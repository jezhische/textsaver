package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.TextPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TextPartServiceImpl implements TextPartService {

    private TextPartRepository repository;

    @Autowired
    public TextPartServiceImpl(TextPartRepository repository) {
        this.repository = repository;
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
//    public Page<TextPart> findSortedPageByNumber(Integer pageNumber, Pageable pageable) {
//        return repository.findSortedPageById(pageNumber, pageable);
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
    public Optional<Date> updateById(Long id, String body, Date updated) {
        return repository.updateById(id, body, updated);
    }

    @Override
    public Optional<TextPart> update(TextPart textPart) {
        return Optional.of(repository.saveAndFlush(textPart));
    }

// ==================================================================================================================

    @Override
    public Page<TextPart> findPageByDocDataIdAndPageNumber(Long textCommonDataId, int pageNumber) {
        return repository.findPageByDocDataId(textCommonDataId, PageRequest.of(pageNumber - 1, 1));
    }

    @Override
    public Page<TextPart> createPage(int currentPageNumber, Long textCommonDataId)
            throws IndexOutOfBoundsException {

        TextPart current = repository.findPageByDocDataId(textCommonDataId,
                PageRequest.of(currentPageNumber - 1, 1)).getContent().get(0);
        TextPart next = repository.findNextByCurrentInSequence(current).orElse(null);
        TextCommonData textCommonData = current.getTextCommonData();
        TextPart newOne = TextPart.builder()
                .lastUpdate(new Date())
                .textCommonData(textCommonData)
                .build(); // in this moment without nextItem to avoid constraint "unique next_item" violation
        repository.saveAndFlush(newOne);
        current.setNextItem(newOne.getId());
        newOne.setNextItem(next == null ? null : next.getId());
        return findPageByDocDataIdAndPageNumber(textCommonDataId, currentPageNumber + 1);
    }
}
