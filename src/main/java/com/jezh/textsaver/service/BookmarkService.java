package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.Bookmarks;

import java.util.Optional;

public interface BookmarkService {

//    List<Bookmarks> findAll();

//    Bookmarks create(Bookmarks newOne);

    Optional<Bookmarks> findById(Long id);

    Optional<Bookmarks> update(Bookmarks bookmarks);

//    Bookmarks delete(Bookmarks bookmarks);
}
