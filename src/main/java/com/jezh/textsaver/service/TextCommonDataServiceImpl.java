package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.repository.TextCommonDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TextCommonDataServiceImpl implements TextCommonDataService {

    @Autowired
    private TextCommonDataRepository repository;


    @Override
    public TextCommonData create(TextCommonData textCommonData) {
        return repository.saveAndFlush(textCommonData);
    }

    @Override
    public TextCommonData update(TextCommonData textCommonData) {
        return repository.saveAndFlush(textCommonData);
    }

    @Override
    public TextCommonData getOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Optional<TextCommonData> findTextPartById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<TextCommonData> findAll() {
        return repository.findAll();
    }
}
