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

    private final TextCommonDataService textCommonDataService;

    private final DataManager dataManager;

    private final Environment environment;

    private final TextPartService textPartService;

    private final BookmarkService bookmarkService;

    private final TextCommonDataResourceAssembler dataAssembler;

    private final BookmarkResourceAssembler pageModelAssembler;

    @Autowired
    public TextCommonDataController(TextCommonDataService textCommonDataService, DataManager dataManager, Environment environment, TextPartService textPartService, BookmarkService bookmarkService, TextCommonDataResourceAssembler dataAssembler, BookmarkResourceAssembler pageModelAssembler) {
        this.textCommonDataService = textCommonDataService;
        this.dataManager = dataManager;
        this.environment = environment;
        this.textPartService = textPartService;
        this.bookmarkService = bookmarkService;
        this.dataAssembler = dataAssembler;
        this.pageModelAssembler = pageModelAssembler;
    }

//    @ResponseBody
//    @PostMapping("")
//    public HttpEntity<TextCommonDataResource> create(@RequestBody String username)
//            throws NoHandlerFoundException, UnknownHostException {
//        TextCommonData textCommonData = textCommonDataService.create(username);
//        return ResponseEntity.ok(dataAssembler.convertToLinkedRepresentation(textCommonData));
//    }

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

//    /** when the application started, redirect to home page */
//    @GetMapping(path = "")
//    public String getHomePage() {
//        return "redirect:/documents";
//    }

    /** find all the textCommonData, assign the controller transient data repository fields and return list of models
     * with the links to the saved texts for the html page */
    // здесь получаю набор ссылок на сохраненные документы, которые будут расположены в левой части экрана. Запрос от
    // каждой из них будет выглядеть как "http://localhost/documents/TextCommonDataRepositoryPostgresTest-created-date-..."
    // Значит, при нажатии на ссылку нужно обслужить запрос "documents/{doc-username}" и получить страницу с rel
    // "self=http://localhost/documents/TextCommonDataRepositoryPostgresTest-created-date-.../pages?page=..."
//    @ResponseBody
//    @GetMapping(path = "/documents")
//    public List<TextCommonDataResource> getLinkedTextCommonDataList() {
//        List<TextCommonData> docsData = textCommonDataService.findAll();
//        List<TextCommonDataResource> linkedDocsData =
//                dataManager.setTextCommonDataControllerTransientDataRepo(docsData);
//        return linkedDocsData;
//        }


//    /** find by username in conjunction with created date */
// Обслужить запрос "http://localhost/documents/TextCommonDataRepositoryPostgresTest-created-date-...": получить список
// закладок, к которому каждый раз будет обращаться страница (через BookmarkService), получить номер последней
// открывавшейся страницы и сделать forward
// на "http://localhost/documents/TextCommonDataRepositoryPostgresTest-created-date-.../pages?page=..."
//    @ResponseBody
//    @GetMapping(value = "documents/{doc-username}")
//    public String findByLinkedDocName(
//            @PathVariable(value = "doc-username") String username) {
//
//        // FIXME: проверка на null и бросить исключение (get возвращает null, если не нашел по ключу)
//        Long docId = dataManager.getDocIds().get(username);
//
//// todo: тут он заполняет репозиторий контроллера страниц, получает закладки, получает последнюю открытую страницу, открывает ее вместе со всеми ссылками
//// Закладки нужно получать каждый раз при открытии страницы, так что просто включить вызов BookmarkService в метод TextPartController
//
//        return "forward:/text-common-data/" + docId + "/text-parts";
//    }

}
