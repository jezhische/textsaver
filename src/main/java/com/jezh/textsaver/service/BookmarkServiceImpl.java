package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.repository.BookmarkRepository;
import com.jezh.textsaver.repository.TextCommonDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {

    private BookmarkRepository bookmarkRepository;

    private TextCommonDataRepository textCommonDataRepository;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository,
                               TextCommonDataRepository textCommonDataRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.textCommonDataRepository = textCommonDataRepository;
    }

    @Override
    public Bookmarks create(Bookmarks newOne) {
        return bookmarkRepository.saveAndFlush(newOne);
    }

    @Override
    public Optional<Bookmarks> findById(Long id) {
        return bookmarkRepository.findById(id);
    }

    //    @Override
//    public Bookmarks delete(Bookmarks bookmarks) {
//        bookmarkRepository
//        return null;
//    }
}
