package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.Bookmark;

import java.util.List;

public interface BookmarkService {

//    List<Bookmark> findAll();

    List<Bookmark> getAllInSortedOrder(Long textCommonDataId);

    Bookmark create(Bookmark current, Bookmark newOne, Bookmark next);

    Bookmark create(Bookmark newOne);

//    Bookmark delete(Bookmark bookmark);
}
