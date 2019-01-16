package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextPart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.UnknownHostException;
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

    Optional<TextPart> findTextPartById(Long id);

    List<Long> findSortedTextPartIdByTextCommonDataId(Long textCommonDataId);

    Optional<Date> updateById(Long textPartId, String body, Date updated);

//    Optional<TextPart> update(Long textPartId);
    Optional<TextPart> update(TextPart textPart);

// ===================================================================================================

    Page<TextPart> findPageByDocDataIdAndPageNumber(Long textCommonDataId, int currentPageNumber);

    Page<TextPart> createPage(int newPageNumber, /*int nextPageNumber, */Long textCommonDataId) throws UnknownHostException, NoHandlerFoundException;

    void delete(long dataId, int pageNm) throws NoHandlerFoundException, UnknownHostException;
}
