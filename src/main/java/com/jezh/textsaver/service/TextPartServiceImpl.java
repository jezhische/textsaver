package com.jezh.textsaver.service;

import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.BookmarkRepository;
import com.jezh.textsaver.repository.TextPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TextPartServiceImpl implements TextPartService {

    private final TextPartRepository repository;

    private final BookmarkRepository bookmarkRepository;

    private final DataManager dataManager;

    @Autowired
    public TextPartServiceImpl(TextPartRepository repository, BookmarkRepository bookmarkRepository, DataManager dataManager) {
        this.repository = repository;
        this.bookmarkRepository = bookmarkRepository;
        this.dataManager = dataManager;
    }

    @Override
    public TextPart create(TextPart textPart) {
        return repository.saveAndFlush(textPart);
    }

    @Override
    public TextPart create(TextPart current, TextPart newOne, TextPart next) {
        if (next != null) newOne.setNextItem(next.getId());
        repository.saveAndFlush(newOne);
        if (current != null) {
            current.setNextItem(newOne.getId());
            repository.saveAndFlush(current);
        }
        return newOne;
    }

//    @Override
//    public TextPart update(TextPart textPart) {
//        return repository.saveAndFlush(textPart);
//    }

    @Override
    public TextPart getOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Optional<TextPart> findByNextItem(Long nextItem) {
        return repository.findByNextItem(nextItem);
    }

    @Override
    public List<TextPart> findAll() {
        return repository.findAll();
    }

    /** find all the textParts with given textCommonDataId */
    @Override
    public List<TextPart> findSortedSetByTextCommonDataId(Long textCommonDataId) {
        return repository.findSortedSetByTextCommonDataId(textCommonDataId);
    }

    /** find a bunch of sequenced textParts with 'size' quantity and starting with 'startId' id */
    @Override
    public List<TextPart> findSortedTextPartBunchByStartId(Long startId, int size) {
        return repository.findSortedTextPartBunchByStartId(startId, size);
    }

    /** find a bunch of sequenced textParts, starting with 'startId' id up to the end of list */
    @Override
    public List<TextPart> findRemainingSortedTextPartBunchByStartId(Long startId) {
        return repository.findRemainingSortedTextPartBunchByStartId(startId);
    }

    @Override
    public Optional<TextPart> findNextByCurrentInSequence(TextPart current) {
        Long nextId = current.getNextItem();
        return repository.findTextPartById(nextId);
    }

    @Override
    public Optional<TextPart> findPreviousByCurrentInSequence(TextPart current) {
        return repository.findPreviousByCurrentInSequence(current);
    }

// =========================================================================


//    @Override
//    public Page<TextPart> findSortedPageByNumber(Integer currentPageNumber, Pageable pageable) {
//        return repository.findSortedPageById(currentPageNumber, pageable);
//    }

    @Override
    public Optional<TextPart> findTextPartById(Long id) {
        return repository.findTextPartById(id);
    }

    // NB: id BIGSERIAL PRIMARY KEY will be cast to BigInteger and cannot be cast to Long automatically
    @Override
    public List<Long> findSortedTextPartIdByTextCommonDataId(Long textCommonDataId) {
        List<BigInteger> obtained = repository.findSortedTextPartIdByTextCommonDataId(textCommonDataId);
        List<Long> desired = new ArrayList<>();
        obtained.forEach(bigInteger -> desired.add(bigInteger.longValueExact()));
        return desired;
    }


    @Override
    public Optional<TextPart> update(TextPart textPart) {
        return Optional.of(repository.saveAndFlush(textPart));
    }

// ==================================================================================================================

    @Override
    public Optional<Date> updateById(Long textPartId, String body, Date updated) {
        return repository.updateById(textPartId, body, updated);
    }

    @Override
    public Page<TextPart> findPageByDocDataIdAndPageNumber(Long docDataId, int currentPageNumber) {
        // the Page is the list that can consist of several db entries; in my case I use one entry per page only
        return repository.findPageByDocDataId(docDataId, PageRequest.of(currentPageNumber, 1));
    }

    @Override
    public Page<TextPart> createPage(int newPageNumber, Long docDataId)
            throws IndexOutOfBoundsException, UnknownHostException, NoHandlerFoundException {

        String url = dataManager.createPageLink(docDataId, newPageNumber);

        int currentPageNm = newPageNumber - 1;
        TextPart current = findPageByDocDataIdAndPageNumber(docDataId, currentPageNm).getContent().get(0);
        TextPart next = repository.findNextByCurrentInSequence(current).orElse(null);
        TextCommonData textCommonData = current.getTextCommonData();
        TextPart newOne = TextPart.builder()
                .lastUpdate(new Date())
                .textCommonData(textCommonData)
                .build();
        // save entry in db without nextItem (i.e. nextItem ) to avoid constraint "unique next_item" violation
        repository.saveAndFlush(newOne);
        current.setNextItem(newOne.getId());
        newOne.setNextItem(next == null ? null : next.getId());

        Bookmarks bookmarks = bookmarkRepository.findById(docDataId)
                .orElseThrow(() -> new NoHandlerFoundException("post", url, new HttpHeaders()));
        dataManager.updateBookmarksForInsertPage(currentPageNm, bookmarks);
        bookmarkRepository.saveAndFlush(bookmarks);

        return repository.findPageByDocDataId(docDataId, PageRequest.of(newPageNumber, 1));
    }

    @Override
    public void delete(long docDataId, int currentPageNm) throws UnknownHostException, NoHandlerFoundException {

        String url = dataManager.createPageLink(docDataId, currentPageNm);
        NoHandlerFoundException noHandlerFoundException = new NoHandlerFoundException("delete", url, new HttpHeaders());

        TextPart current = findPageByDocDataIdAndPageNumber(docDataId, currentPageNm)
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> noHandlerFoundException);

        TextPart previous = repository.findPreviousByCurrentInSequence(current).orElseThrow(() -> noHandlerFoundException);

        TextCommonData textCommonData = current.getTextCommonData();
        textCommonData.setUpdatedDate(new Date());

        Bookmarks bookmarks = bookmarkRepository.findById(docDataId)
                .orElseThrow(() -> noHandlerFoundException);
        dataManager.deleteBookmark(currentPageNm, bookmarks);
        bookmarkRepository.saveAndFlush(bookmarks);

        Long nextItem = current.getNextItem();

        current.setNextItem(null);
        repository.saveAndFlush(current);

        previous.setNextItem(nextItem);
//        repository.saveAndFlush(previous);
        repository.delete(current);
    }
}
