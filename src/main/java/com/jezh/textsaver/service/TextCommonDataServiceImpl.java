package com.jezh.textsaver.service;

import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.BookmarkRepository;
import com.jezh.textsaver.repository.TextCommonDataRepository;
import com.jezh.textsaver.repository.TextPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TextCommonDataServiceImpl implements TextCommonDataService {

    private TextCommonDataRepository textCommonDataRepository;

    private BookmarkRepository bookmarkRepository;

    private TextPartRepository textPartRepository;

    private static final int BOOKMARKS_COUNT = 10;

    @Autowired
    public TextCommonDataServiceImpl(TextCommonDataRepository textCommonDataRepository,
                                     BookmarkRepository bookmarkRepository,
                                     TextPartRepository textPartRepository) {
        this.textCommonDataRepository = textCommonDataRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.textPartRepository = textPartRepository;
    }


    @Override
    public List<TextCommonData> findAllByOrderByNameCreatedDateAsc() {
        return textCommonDataRepository.findAllByOrderByNameAsc();
    }

    @Override
    public TextCommonData create(String name) {
        // create textCommonData
        Date createdDate = new Date();
        TextCommonData textCommonData = TextCommonData.builder()
                .name(DataManager.trimQuotes(name))
                .createdDate(createdDate)
                .updatedDate(createdDate)
                .build();
        textCommonDataRepository.saveAndFlush(textCommonData);

        // create textPart
        TextPart textPart = TextPart.builder().lastUpdate(createdDate).build();
        textPart.setTextCommonData(textCommonData);
        textPartRepository.saveAndFlush(textPart);

        // create bookmarks
        String[] lastOpens = new String[BOOKMARKS_COUNT];
        lastOpens[0] = DataManager.getLastOpenedArrayItem(1, false);
        Bookmarks bookmarks = Bookmarks.builder()
                .lastOpenArray(lastOpens)
                .textCommonData(textCommonData)
                .build();
        bookmarkRepository.saveAndFlush(bookmarks);

        // update textCommonData
        textCommonData.setBookmarks(bookmarks);
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
}
