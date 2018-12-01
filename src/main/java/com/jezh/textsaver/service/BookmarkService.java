package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.Bookmark;

import java.util.List;
import java.util.Optional;

public interface BookmarkService {

//    List<Bookmark> findAll();

    Bookmark create(Bookmark newOne);

    Optional<Bookmark> findById(Long id);

//    Bookmark delete(Bookmark bookmark);
}
