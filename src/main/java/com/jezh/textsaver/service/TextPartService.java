package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.TextPartRepository;

import java.util.List;
import java.util.Optional;

public interface TextPartService {
    TextPart create(TextPart textPart);
    TextPart create(TextPart current, TextPart newOne, TextPart next);
    TextPart update(TextPart textPart);
    TextPart getOne(Long id);
    Optional<TextPart> findTextPartById(Long id);
    List<TextPart> findAll();

    List<TextPart> findByTextCommonDataId(Long textCommonDataId);

    Optional<TextPart> findNextByCurrentInSequence(TextPart current);
    Optional<TextPart> findPreviousByCurrentInSequence(TextPart current);
//    void delete(TextPart textPart);
//    void deleteAll();
//    void deleteById(Long id);

}
