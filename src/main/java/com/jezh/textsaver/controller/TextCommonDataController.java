package com.jezh.textsaver.controller;

import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.exceptions.EntityNotFoundException;
import com.jezh.textsaver.service.TextCommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/text-common-data")
public class TextCommonDataController {

    @Autowired
    private TextCommonDataService textCommonDataService;

    @GetMapping(path = "")
    public ResponseEntity<?> getTextCommonDataList() {
        return ResponseEntity.ok(textCommonDataService.findAll());
    }

    @GetMapping(path = "/{commonDataId}")
    public ResponseEntity<TextCommonData> findTextCommonDataById(@PathVariable Long id) {
        TextCommonData textCommonData = null;
        try {
            textCommonData = textCommonDataService.findTextPartById(id)
                    .orElseThrow(() -> new EntityNotFoundException("textPart with such id is not found", new SQLException()));
        } catch (EntityNotFoundException ex) {return ResponseEntity.notFound().build();}
        return ResponseEntity.ok().body(textCommonData);
    }
}
