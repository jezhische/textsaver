package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextCommonData;

import java.util.List;
import java.util.Optional;

public interface TextCommonDataService {
    List<TextCommonData> findAllByOrderByNameCreatedDateAsc();
    TextCommonData create(String name);
    TextCommonData update(TextCommonData textCommonData);
    TextCommonData getOne(Long id);
    Optional<TextCommonData> findTextCommonDataById(Long id);
    List<TextCommonData> findAll();
}
