package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.Bookmark;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.BookmarkRepository;
import com.jezh.textsaver.repository.DocumentDataRepository;
import com.jezh.textsaver.repository.TextCommonDataRepository;
import com.jezh.textsaver.repository.TextPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TextCommonDataServiceImpl implements TextCommonDataService {

    private TextCommonDataRepository textCommonDataRepository;

    private BookmarkRepository bookmarkRepository;

    private TextPartRepository textPartRepository;

    private static final int bookmarksCount = 10;

    @Autowired
    public TextCommonDataServiceImpl(TextCommonDataRepository textCommonDataRepository,
                                     BookmarkRepository bookmarkRepository,
                                     TextPartRepository textPartRepository) {
        this.textCommonDataRepository = textCommonDataRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.textPartRepository = textPartRepository;
    }


    @Override
    public TextCommonData create(String name) {
        // create textCommonData
        TextCommonData textCommonData = DocumentDataRepository.createTextCommonData(name);
        textCommonDataRepository.saveAndFlush(textCommonData);

        // create textPart
        TextPart textPart = DocumentDataRepository.createTextPart(textCommonData);
        textPartRepository.saveAndFlush(textPart);

        // create bookmarks
        Bookmark bookmark = DocumentDataRepository.createBookmark(bookmarksCount, textCommonData);
        bookmarkRepository.saveAndFlush(bookmark);

        // update textCommonData
//        textCommonData.setBookmark(bookmark);
        textCommonData.setFirstItem(textPart.getId());
        return textCommonData;
    }

    @Override
    public TextCommonData update(TextCommonData textCommonData) {
        return textCommonDataRepository.saveAndFlush(textCommonData);
    }

    @Override
    public TextCommonData getOne(Long id) {
        return textCommonDataRepository.getOne(id);
    }

    @Override
    public Optional<TextCommonData> findTextCommonDataById(Long id) {
        return textCommonDataRepository.findById(id);
    }

    @Override
    public List<TextCommonData> findAll() {
        return textCommonDataRepository.findAll();
    }

// ====================================================================================================


}
