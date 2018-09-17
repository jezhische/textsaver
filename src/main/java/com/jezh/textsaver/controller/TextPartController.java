package com.jezh.textsaver.controller;

import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.exceptions.EntityNotFoundException;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.sql.SQLException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/text-common-data/{commonDataId}")
public class TextPartController {

    @Autowired
    private TextPartService textPartService;

    @Autowired
    private TextCommonDataService textCommonDataService;

    @PostMapping(path = "/text-parts")
    public ResponseEntity<TextPart> create(@Valid @RequestBody TextPart textPart,
                                           UriComponentsBuilder uriBuilder,
                                           BindingResult bindingResult) {
        textPartService.create(textPart);
        HttpHeaders headers = new HttpHeaders();

//        UriComponents uriComponents = UriComponentsBuilder
//                .fromUriString("http://example.com/hotels/{hotel}")
//                .queryParam("q", "{q}")
//                .encode()
//                .build();
//        URI uri = uriComponents.expand("Westin", "123").toUri();

        headers.setLocation(uriBuilder.path("/text-parts/{textPartId}").encode().buildAndExpand(textPart.getId()).toUri());
        return new ResponseEntity<TextPart>(headers, HttpStatus.CREATED);
    }

    @GetMapping (path = "/text-parts")
    public ResponseEntity<?> getTextPartListByTextCommonDataId(@PathVariable("commonDataId") Long textCommonDataId) {
        return ResponseEntity.ok().body(textPartService.findByTextCommonDataId(textCommonDataId));
    }

    @GetMapping (path = "/text-parts/{textPartId}")
    public ResponseEntity<TextPart> findTextPartById(@PathVariable Long textPartId) {
        TextPart textPart = null;
        try {
        textPart = textPartService.findTextPartById(textPartId)
                .orElseThrow(() -> new EntityNotFoundException("textPart with such id is not found", new SQLException()));
        } catch (EntityNotFoundException ex) {return ResponseEntity.notFound().build();}
        return ResponseEntity.ok().body(textPart);
    }

//    @GetMapping (path = "/text-parts/{id}")
//    public ResponseEntity<TextPart> getTextPartById() {
//
//    }
}
