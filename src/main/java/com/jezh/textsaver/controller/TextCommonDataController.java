package com.jezh.textsaver.controller;

import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.exception.ResNotFoundException;
import com.jezh.textsaver.service.TextCommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@RequestMapping("/text-common-data")
public class TextCommonDataController {

    @Autowired
    private TextCommonDataService textCommonDataService;

    /** find all the textCommonData */
    @GetMapping(path = "")
    public ResponseEntity<?> getTextCommonDataList() {
        return ResponseEntity.ok(textCommonDataService.findAll());
    }

    /** find the textCommonData by id */
    @GetMapping(path = "/{commonDataId}")
    public ResponseEntity<TextCommonData> findTextCommonDataById(@PathVariable("commonDataId") Long id,
                                                                 HttpServletRequest request) throws NoHandlerFoundException {
        TextCommonData textCommonData = null;
            textCommonData = textCommonDataService.findTextCommonDataById(id)
                    .orElseThrow(() ->
//                            new ResNotFoundException("textPart with such id is not found", new SQLException()));
                            new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), new HttpHeaders()));
        return ResponseEntity.ok().body(textCommonData);
    }
}
