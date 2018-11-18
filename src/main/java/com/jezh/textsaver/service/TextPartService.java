package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextPart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TextPartService {
    TextPart create(TextPart textPart);
    TextPart create(TextPart current, TextPart newOne, TextPart next);
    TextPart getOne(Long id);
    Optional<TextPart> findByNextItem(Long nextItem);

    List<TextPart> findAll();

    Optional<TextPart> findNextByCurrentInSequence(TextPart current);
    Optional<TextPart> findPreviousByCurrentInSequence(TextPart current);


    List<TextPart> findSortedSetByTextCommonDataId(Long textCommonDataId);

    List<TextPart> findSortedTextPartBunchByStartId(Long startId, int size);

    List<TextPart> findRemainingSortedTextPartBunchByStartId(Long startId);
//    void delete(TextPart textPart);
//    void deleteAll();
//    void deleteById(Long id);

// ====================================================================
    Page<TextPart> findSortedPagesByTextCommonDataId(Long textCommonDataId, Pageable pageable);

    Optional<TextPart> findTextPartById(Long id);

    List<Long> findSortedTextPartIdByTextCommonDataId(Long textCommonDataId);

    Optional<Date> updateById(Long id, String body, Date updated);

//    Optional<TextPart> update(Long textPartId);
    Optional<TextPart> update(TextPart textPart);


}
