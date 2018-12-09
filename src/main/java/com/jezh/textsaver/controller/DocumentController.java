package com.jezh.textsaver.controller;

import com.jezh.textsaver.service.TextCommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller(value = "/documents")
public class DocumentController {

    private TextCommonDataService textCommonDataService;

    @Autowired
    public DocumentController(TextCommonDataService textCommonDataService) {
        this.textCommonDataService = textCommonDataService;
    }

    @PostMapping
    @ResponseBody public /*HttpEntity<?>*/ void createDoc(@RequestBody String name) {
        textCommonDataService.create(name);
    }
}
