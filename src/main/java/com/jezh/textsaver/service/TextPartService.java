package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.TextPartRepository;

import java.util.List;
import java.util.Optional;

public interface TextPartService {
    TextPart create(TextPart textPart);
    TextPart update(TextPart textPart);
    TextPart getOne(Long id);
    Optional<TextPart> findTextPartById(Long id);
    List<TextPart> findAll();
}
