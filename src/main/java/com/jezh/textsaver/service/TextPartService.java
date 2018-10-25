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
    Optional<TextPart> findByNextItem(Long nextItem);

    List<TextPart> findAll();

    Optional<TextPart> findNextByCurrentInSequence(TextPart current);
    Optional<TextPart> findPreviousByCurrentInSequence(TextPart current);


    List<TextPart> findSortedSetByTextCommonDataId(Long textCommonDataId);

    List<TextPart> findSortedTextPartBunchByStartId(Long startId, int size);
//    void delete(TextPart textPart);
//    void deleteAll();
//    void deleteById(Long id);

}
