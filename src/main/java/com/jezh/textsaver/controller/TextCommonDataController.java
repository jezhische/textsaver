package com.jezh.textsaver.controller;

import com.jezh.textsaver.businessLayer.TextCommonDataLinkAssembler;
import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.businessLayer.TextPartResourceAssembler;
import com.jezh.textsaver.dto.TextPartResource;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.BookmarkService;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
//@RequestMapping("/text-common-data")
@RequestMapping("/doc-data")
public class TextCommonDataController {

    @Autowired
    private TextCommonDataService textCommonDataService;

    @Autowired
    private TextPartService textPartService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private TextCommonDataLinkAssembler dataAssembler;

    @Autowired
    private TextPartResourceAssembler pageModelAssembler;

    @ResponseBody
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody String name) throws NoHandlerFoundException {
        TextCommonData textCommonData = textCommonDataService.create(name);
        long id = textCommonData.getId();
        Page<TextPart> page = textPartService.findPageByDocDataIdAndPageNumber(id, 1);
        Bookmarks bookmarks = bookmarkService.findById(id).orElseThrow(() -> new NoHandlerFoundException("GET",
                "/doc-data", new HttpHeaders()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pageModelAssembler.getResource(page, bookmarks));
    }



//    /** when the application started, redirect to home page */
//    @GetMapping(path = "")
//    public String getHomePage() {
//        return "redirect:/documents";
//    }

    /** find all the textCommonData, assign the controller transient data repository fields and return list of models
     * with the links to the saved texts for the html page */
    // здесь получаю набор ссылок на сохраненные документы, которые будут расположены в левой части экрана. Запрос от
    // каждой из них будет выглядеть как "http://localhost/documents/TextCommonDataRepositoryPostgresTest-created-date-..."
    // Значит, при нажатии на ссылку нужно обслужить запрос "documents/{doc-name}" и получить страницу с rel
    // "self=http://localhost/documents/TextCommonDataRepositoryPostgresTest-created-date-.../pages?page=..."
//    @ResponseBody
//    @GetMapping(path = "/documents")
//    public List<TextCommonDataResource> getLinkedTextCommonDataList() {
//        List<TextCommonData> docsData = textCommonDataService.findAll();
//        List<TextCommonDataResource> linkedDocsData =
//                dataManager.setTextCommonDataControllerTransientDataRepo(docsData);
//        return linkedDocsData;
//        }


//    /** find by name in conjunction with created date */
// Обслужить запрос "http://localhost/documents/TextCommonDataRepositoryPostgresTest-created-date-...": получить список
// закладок, к которому каждый раз будет обращаться страница (через BookmarkService), получить номер последней
// открывавшейся страницы и сделать forward
// на "http://localhost/documents/TextCommonDataRepositoryPostgresTest-created-date-.../pages?page=..."
//    @ResponseBody
//    @GetMapping(value = "documents/{doc-name}")
//    public String findByLinkedDocName(
//            @PathVariable(value = "doc-name") String name) {
//
//        // FIXME: проверка на null и бросить исключение (get возвращает null, если не нашел по ключу)
//        Long docId = dataManager.getDocIds().get(name);
//
//// todo: тут он заполняет репозиторий контроллера страниц, получает закладки, получает последнюю открытую страницу, открывает ее вместе со всеми ссылками
//// Закладки нужно получать каждый раз при открытии страницы, так что просто включить вызов BookmarkService в метод TextPartController
//
//        return "forward:/text-common-data/" + docId + "/text-parts";
//    }


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
