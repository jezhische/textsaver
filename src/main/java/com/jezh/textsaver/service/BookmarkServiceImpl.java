package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.Bookmark;
import com.jezh.textsaver.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {

    private BookmarkRepository repository;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository repository) {
        this.repository = repository;
    }

    @Override
    public Bookmark create(Bookmark newOne) {
        return repository.saveAndFlush(newOne);
    }

//    @Override
//    public Bookmark delete(Bookmark bookmark) {
//        repository
//        return null;
//    }
}
