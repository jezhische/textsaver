package com.jezh.textsaver.controller;

import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.businessLayer.TextCommonDataResourceAssembler;
import com.jezh.textsaver.businessLayer.BookmarkResourceAssembler;
import com.jezh.textsaver.dto.TextCommonDataResource;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.service.BookmarkService;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.UnknownHostException;
import java.util.List;

@Controller
@RequestMapping("/doc-data")
public class TextCommonDataController {

    @Autowired
    private TextCommonDataService textCommonDataService;

    @Autowired
    private TextCommonDataResourceAssembler dataAssembler;

// ================================================================================================================ POST:

    @ResponseBody
    @PostMapping("")
    public HttpEntity<TextCommonDataResource> create(@RequestBody String name)
            throws NoHandlerFoundException, UnknownHostException {
        TextCommonData textCommonData = textCommonDataService.create(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(dataAssembler.convertToLinkedRepresentation(textCommonData));
    }

// ================================================================================================================ GET:

    @ResponseBody
    @GetMapping("")
    public HttpEntity<List<TextCommonDataResource>> getDocs() {
        return ResponseEntity.ok(dataAssembler.getLinkedDocsData(textCommonDataService.findAllByOrderByNameCreatedDateAsc()));
    }
}
