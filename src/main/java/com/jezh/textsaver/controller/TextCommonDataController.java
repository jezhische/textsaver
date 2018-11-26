package com.jezh.textsaver.controller;

import com.jezh.textsaver.businessLayer.TextCommonDataLinkAssembler;
import com.jezh.textsaver.businessLayer.UtilManager;
import com.jezh.textsaver.dto.TextCommonDataControllerTransientDataRepo;
import com.jezh.textsaver.dto.TextCommonDataLinkedRepresentation;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.service.TextCommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
//@RequestMapping("/text-common-data")
public class TextCommonDataController {

    @Autowired
    private TextCommonDataService textCommonDataService;

    @Autowired
    private TextCommonDataLinkAssembler assembler;

    @Autowired
    private TextCommonDataControllerTransientDataRepo repository;



//    /** when the application started, redirect to home page */
//    @GetMapping(path = "")
//    public String getHomePage() {
//        return "redirect:/documents";
//    }

    /** find all the textCommonData, assign the controller transient data repository fields and return list of models
     * with everything needed to build links to the saved texts on the html page */
    @ResponseBody
    @GetMapping(path = "/documents")
    public List<TextCommonDataLinkedRepresentation> getLinkedTextCommonDataList() {
        List<TextCommonData> docsData = textCommonDataService.findAll();
        List<TextCommonDataLinkedRepresentation> linkedDocsData = assembler.getLinkedDocsData(docsData);
        repository = TextCommonDataControllerTransientDataRepo.builder()
                .docIds(new HashMap<>())
                .build();
        UtilManager.setTextCommonDataControllerTransientDataRepo(docsData, linkedDocsData, repository);
        return linkedDocsData;
        }


    /** find by name in conjunction with created date */
//    @ResponseBody
    @GetMapping(value = "documents/{doc-name}")
    public String findByLinkedDocName(
            @PathVariable(value = "doc-name") String name) {

        Long docId = repository.getDocIds().get(name);
// FIXME: проверка на null и бросить исключение
        return "redirect:/text-common-data/" + docId + "/text-parts"; //TODO: может быть, не redirect, а forward, чтобы не было
        // лишнего перекидывания запросами между сервером и клиентом? Почитать!
    }


    /** find the textCommonData by id */
//    @ResponseBody
//    @GetMapping(path = "text-common-data/{commonDataId}")
//    public ResponseEntity<TextCommonData> findTextCommonDataById(@PathVariable("commonDataId") Long id,
//                                                                 HttpServletRequest request) throws NoHandlerFoundException {
//        TextCommonData textCommonData = null;
//            textCommonData = textCommonDataService.findTextCommonDataById(id)
//                    .orElseThrow(() ->
////                            new ResNotFoundException("textPart with such id is not found", new SQLException()));
//                            new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), new HttpHeaders()));
//        return ResponseEntity.ok().body(textCommonData);
//    }
}
