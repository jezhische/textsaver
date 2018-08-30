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
    public List<TextPart> findAll() {
        return repository.findAll();
    }
}
