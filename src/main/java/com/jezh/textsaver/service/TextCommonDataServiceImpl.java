package com.jezh.textsaver.service;

import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.AppUserRepository;
import com.jezh.textsaver.repository.BookmarkRepository;
import com.jezh.textsaver.repository.TextCommonDataRepository;
import com.jezh.textsaver.repository.TextPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TextCommonDataServiceImpl implements TextCommonDataService {

    private final TextCommonDataRepository textCommonDataRepository;

    private final BookmarkRepository bookmarkRepository;

    private final TextPartRepository textPartRepository;

    private final AppUserRepository appUserRepository;

//    private static final int BOOKMARKS_COUNT = 10;

    @Autowired
    public TextCommonDataServiceImpl(TextCommonDataRepository textCommonDataRepository,
                                     BookmarkRepository bookmarkRepository,
                                     TextPartRepository textPartRepository,
                                     AppUserRepository appUserRepository) {
        this.textCommonDataRepository = textCommonDataRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.textPartRepository = textPartRepository;
        this.appUserRepository = appUserRepository;
    }


    @Override
    public List<TextCommonData> findAllByOrderByNameCreatedDateAsc() {
        AppUser authenticated = appUserRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());
        return textCommonDataRepository.findAllByUserOrderByNameAsc(authenticated);
    }

    @Override
    public TextCommonData create(String name) {
        // create textCommonData
        Date createdDate = new Date();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        AppUser principal = (AppUser) auth.getPrincipal();
        TextCommonData textCommonData = TextCommonData.builder()
                .name(DataManager.trimQuotes(name))
                .user(appUserRepository.findByUsername(auth.getName()))
                .createdDate(createdDate)
                .updatedDate(createdDate)
                .build();
        textCommonDataRepository.saveAndFlush(textCommonData);

        // create textPart
        TextPart textPart = TextPart.builder().lastUpdate(createdDate).build();
        textPart.setTextCommonData(textCommonData);
        textPartRepository.saveAndFlush(textPart);

        // create bookmarks
//        String[] lastOpens = new String[BOOKMARKS_COUNT];
        String[] lastOpens = new String[1];
        lastOpens[0] = DataManager.getLastOpenedArrayItem(0, false);
        Bookmarks bookmarks = Bookmarks.builder()
                .lastOpenArray(lastOpens)
                .specialBookmarks(new int[0])
                .textCommonData(textCommonData)
                .build();
        bookmarkRepository.saveAndFlush(bookmarks);

        // update textCommonData
//        textCommonData.setBookmarks(bookmarks);
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
