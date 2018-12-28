package com.jezh.textsaver.controller;

import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.businessLayer.PageResourceAssembler;
import com.jezh.textsaver.businessLayer.TextPartResourceAssembler;
import com.jezh.textsaver.dto.PageResource;
import com.jezh.textsaver.dto.TextPartResource;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.BookmarkService;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import com.jezh.textsaver.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@RestController
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
@RequestMapping("/doc-data/{commonDataId}")
public class TextPartController {

    @Autowired private TextPartService textPartService;

    @Autowired private TextCommonDataService textCommonDataService;

    @Autowired private BookmarkService bookmarkService;

    @Autowired private PageResourceAssembler pageModelAssembler;

    @Autowired private Environment env;

    @Autowired private DataManager dataManager;

    // the same as @Value("${local.server.port}")
    @LocalServerPort
    private int port;


// ================================================================================================================ GET:
    /** find page model by textCommonData id and page number */
    @GetMapping(value = "/pages", params = {"page"})
    public ResponseEntity<PageResource> findTextPartById(@PathVariable(value = "commonDataId") long id,
                                                         @RequestParam(value = "page") int pageNumber)
            throws NoHandlerFoundException, UnknownHostException {
        Page<TextPart> page = textPartService.findPageByDocDataIdAndPageNumber(id, pageNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(pageModelAssembler.getResource(page));
    }

// ================================================================================================================ POST:

    /**
     * insert new page after current one
     * */
    @PostMapping(value = "/pages", params = {"page"})
    public HttpEntity<TextPartResource> insertPage(@PathVariable(value = "commonDataId") long docDataId,
                                                   @RequestParam(value = "page") int currentPage) throws NoHandlerFoundException {
////        Page<TextPart> page = textPartService.findPageByDocDataIdAndPageNumber(docDataId, 1);
//        Page<TextPart> createdPage =
//        Bookmarks bookmarks = bookmarkService.findById(docDataId).orElseThrow(() -> new NoHandlerFoundException("GET",
//                "/doc-data", new HttpHeaders()));
//        return ResponseEntity.status(HttpStatus.CREATED).body(pageModelAssembler.getResource(createdPage, bookmarks));
        return null;
    }

// ================================================================================================================ PUT:

    /**
     * update current page
     * */
    @PutMapping(value = "/pages", params = {"page"})
    public void updatePage(@PathVariable(value = "commonDataId") long docDataId,
                                                   @RequestParam(value = "page") int currentPage,
                                                   @RequestBody String pageBody)
            throws NoHandlerFoundException, UnknownHostException {
        String url = dataManager.createPageLink(docDataId, currentPage);
        TextPart textPart = textPartService.findPageByDocDataIdAndPageNumber(docDataId, 1)
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoHandlerFoundException("put", url, new HttpHeaders()));
        textPart.setBody(pageBody);
        textPartService.update(textPart);
    }


// ================================================================================================================ PUT:
//
//    @PutMapping(value = "/text-parts/pages", params = {"page"})
//    public HttpEntity<Date> updatePage(
//            @RequestBody TextPartResource linkedPage,
//            @RequestParam(value = "page") int pageNumber,
//            HttpServletRequest request
//    ) throws NoHandlerFoundException, ParseException {
//        String body = linkedPage.getBody();
//        Date current = textPartService.updateById( // FIXME: 17.11.2018 format date to UTC+2.00
//                repository.getListOfSortedTextPartId().get(pageNumber - 1),
//                body,
//                new Date())
//                .orElseThrow(() -> new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), new HttpHeaders()));
//        return new ResponseEntity<>(current, new HttpHeaders(), HttpStatus.OK);
//    }

//    @GetMapping (path = "/text-parts/{id}")
//    public ResponseEntity<TextPart> getTextPartById() {
//
//    }

//        @GetMapping(value = "/text-parts", params = {"page"})
//        public ResponseEntity<TextPartResource> findPage(@RequestParam("page") int page) {
////            Page<TextPartResource> resultPage =
//        return null;
//        }

//    /**
//     * find a bunch of textPart in the sorted order start from the given textPart id and with the given size.
//     * If request parameter is not specified, find all the textPart with given textCommonDataId in this order
//     */
//    @GetMapping(path = "/text-parts") // FIXME: 24.10.2018 add (NullPointer) exceptions handling
//    public ResponseEntity<List<TextPart>> findSortedTextPartBunchByStartId(
//            @PathVariable("commonDataId") Long textCommonDataId,
//            @RequestParam(value = "startId", required = false) Long startId,
//            @RequestParam(value = "size", required = false) Integer size) { // not int or long, otherwise I get "IllegalStateException:
//        // Optional int parameter 'size' is present but cannot be translated into a null value due to being declared as a primitive type."
//        if (startId == null && size == null) {
//        return ResponseEntity.ok().body(textPartService.findSortedSetByTextCommonDataId(textCommonDataId));
//        } else if (startId != null && size == null) {
//            return ResponseEntity.ok().body(textPartService.findRemainingSortedTextPartBunchByStartId(startId));
//        } else if (startId == null && size != 0) {
//// FIXME: 25.11.2018 нужно вернуть не firstTextPartId, а сущность Bookmarks, вынуть из нее закладки, определить, какая страница пойдет и т.д.
//            Long firstTextPartId = textCommonDataService.findTextCommonDataById(textCommonDataId).get().getFirstItem();
//            return ResponseEntity.ok().body(textPartService.findSortedTextPartBunchByStartId(firstTextPartId, size));
//        } else {
//            return ResponseEntity.ok().body(textPartService.findSortedTextPartBunchByStartId(startId, size));
//        }
//    }

//================================================================================================================ GET:
//                                          fixme: open document, find page by document id (textCommonData id) and page number

//    /**  */
//    @GetMapping(value = "/text-parts")
//    public HttpEntity<TextPartResource> getLastOpenPageByTextCommonDataId(
//            @PathVariable long commonDataId,
//            HttpServletRequest request
//    ) {
//
//    }




// ================================================================================================================ GET:

    /**
     * <b>Open document by its id (textCommonData id). First rendered page is specified by textNumber parameter</b><p>
     * NB that in {@link com.jezh.textsaver.service.TextPartServiceImpl#findPageByDocDataIdAndPageNumber(Long, int)}, when obtaining
     * a page, {@code Pageable} argument is created as {@code PageRequest.of(pageNumber - 1, 1)}
     */
    @GetMapping(value = "/text-parts/pages", params = {"page"})
    public HttpEntity<TextPartResource> findPageByTextCommonDataIdAndPageNumber(
            @PathVariable long commonDataId, // "/text-common-data/{commonDataId}" todo: {commonDataName}
            @RequestParam("page") int pageNumber,
            HttpServletRequest request
    ) throws NoHandlerFoundException {
        Page<TextPart> page = textPartService
                .findPageByDocDataIdAndPageNumber(commonDataId, pageNumber);
        int totalPages = page.getTotalPages();
        // FIXME: 07.12.2018 нужно создать, видимо, особое исключение, или подумать, как бросить это
        if (pageNumber < 1 || pageNumber > totalPages) throw ControllerUtils.getNoHandlerFoundException(request);
        // 'cause page contains 1 or 0 textPart element:
        TextPart textPart = page.getContent().stream().findFirst()
                .orElseThrow(() -> ControllerUtils.getNoHandlerFoundException(request));

        TextPartResource linkedPage = null;
//        try {
//            linkedPage = assembler.getLinkedPage(textPart, commonDataId, pageNumber, request, repository);
//        } catch (Exception e) {
//            throw ControllerUtils.getNoHandlerFoundException(request);
//        }
        return new ResponseEntity<>(linkedPage, HttpStatus.OK);
    }



//================================================================================================================ POST:
//                                                   create fully new textpart (or )

    @PostMapping(value = "/text-parts"/*, consumes = {MediaTypes.HAL_JSON_UTF8_VALUE, MediaTypes.HAL_JSON_VALUE}*/)
    public ResponseEntity<Void> createTextPart(
            /*@Valid */@RequestBody TextPartResource textPartResource,
            UriComponentsBuilder uriBuilder,
            BindingResult bindingResult) {
        // здесь с помощью статического метода Manager проверяем textPartResource на соответствие, ошибки и т.п.
//        TextPart textPart = converter.convertToEntity(textPartResource);
//        TextPart textPartCreated = textPartService.create(textPart);
//        TextPartResource textPartResourceCreated = textPartManager.convertToResource(textPartCreated);
//        HttpHeaders headers = new HttpHeaders();
//        URI uri = uriBuilder
//                        .port(port)
//                        .path("/" + env.getRequiredProperty("server.servlet.context-path") +
//                                "/text-common-data/{commonDataId}/text-parts/{textPartId}")
//                        .encode()
//                        .buildAndExpand(textPartCreated.getTextCommonData().getId(), textPartCreated.getId())
//                        .toUri();
//        headers.setLocation(uri);
//        return new ResponseEntity<TextPartResource>(textPartResourceCreated, headers, HttpStatus.CREATED);

//        headers.add("Location", authorResourceAssembler.linkToSingleResource(savedAuthor).getHref() );
        return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.CREATED);
    }



    /* find all the textParts with given textCommonData id */
//    @GetMapping (path = "/text-parts")
//    public ResponseEntity<?> getTextPartListByTextCommonDataId(@PathVariable("commonDataId") Long textCommonDataId) {
//        return ResponseEntity.ok().body(textPartService.findByTextCommonDataId(textCommonDataId));
//    }



// Что является ресурсом - страница или то, что на ней размещено?
// Страница у меня не может быть ресурсом, поскольку если я уничтожаю, например, сущность на 5-й странице, сама страница
// должна остаться, и на нее будет помещена новая сущность. А если страница - это ресурс, то я потеряю ее навсегда,
// и не будет у меня 5-й страницы.
}
