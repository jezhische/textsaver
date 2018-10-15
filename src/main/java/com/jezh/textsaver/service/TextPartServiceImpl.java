package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.repository.TextPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TextPartServiceImpl implements TextPartService {

    @Autowired
    private TextPartRepository repository;

    @Override
    public TextPart create(TextPart textPart) {
        return repository.saveAndFlush(textPart);
    }

    @Override
    public TextPart create(TextPart current, TextPart newOne, TextPart next) {
        newOne.setPreviousItem(current.getId());
        boolean nextIsNotNull = next != null;
        if (nextIsNotNull) newOne.setNextItem(next.getId());
        repository.saveAndFlush(newOne);
//        current.se
        return null;
    }

    @Override
    public TextPart update(TextPart textPart) {
        return repository.saveAndFlush(textPart);
    }

    @Override
    public TextPart getOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Optional<TextPart> findTextPartById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<TextPart> findByPreviousItem(Long previousItem) {
        return repository.findByPreviousItem(previousItem);
    }

    @Override
    public List<TextPart> findAll() {
        return repository.findAll();
    }

    /** find all the textParts with given textCommonDataId */
    @Override
    public List<TextPart> findSortedSetByTextCommonDataId(Long textCommonDataId) {
        return repository.findSortedSetByTextCommonDataId(textCommonDataId);
    }

    /** find a bunch of sequenced textParts with 'size' quantity and starting with 'startId' id */
    @Override
    public List<TextPart> findSortedTextPartBunchByStartId(Long startId, int size) {
        return repository.findSortedTextPartBunchByStartId(startId, size);
    }

    @Override
    public Optional<TextPart> findNextByCurrentInSequence(TextPart current) {
        return repository.findNextByCurrentInSequence(current);
    }

    @Override
    public Optional<TextPart> findPreviousByCurrentInSequence(TextPart current) {
        return repository.findPreviousByCurrentInSequence(current);
    }

}
