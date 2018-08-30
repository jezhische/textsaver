package com.jezh.textsaver.controller;

import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.TextPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class TextsaverController {

    @Autowired
    private TextPartService textPartService;

    @PostMapping(path = "/text-parts")
    public ResponseEntity<TextPart> create(@RequestBody TextPart textPart, UriComponentsBuilder uriBuilder) {
        textPartService.create(textPart);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.path("/text-parts/{id}").buildAndExpand(textPart.getId()).toUri());
        return new ResponseEntity<TextPart>(headers, HttpStatus.CREATED);
    }

    @GetMapping (path = "/text-parts")
    public ResponseEntity<?> getTextPartList() {
        return ResponseEntity.ok().body(textPartService.findAll());
    }
}
