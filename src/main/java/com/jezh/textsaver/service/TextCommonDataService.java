package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextCommonData;

import java.util.List;
import java.util.Optional;

public interface TextCommonDataService {
    TextCommonData create(TextCommonData textCommonData);
    TextCommonData update(TextCommonData textCommonData);
    TextCommonData getOne(Long id);
    Optional<TextCommonData> findTextCommonDataById(Long id);
    List<TextCommonData> findAll();
}
