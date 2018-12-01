package com.jezh.textsaver.controller;

import com.jezh.textsaver.businessLayer.TextPartLinkAssembler;
import com.jezh.textsaver.businessLayer.TextPartPageRepresentationConverter;
import com.jezh.textsaver.dto.TextPartControllerTransientDataRepo;
import com.jezh.textsaver.dto.TextPartPagedLinkedRepresentation;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
@RequestMapping("/text-common-data/{commonDataId}")
public class TextPartController {

    @Autowired
    private TextPartService textPartService;

    @Autowired
    private TextCommonDataService textCommonDataService;

    @Autowired
    private TextPartControllerTransientDataRepo repository;

    @Autowired
    private TextPartPageRepresentationConverter converter;

    @Autowired
    TextPartLinkAssembler assembler;

    @Autowired
    private Environment env;

    // the same as @Value("${local.server.port}")
    @LocalServerPort
    private int port;



    /** find textPart by id */
    @GetMapping (path = "/text-parts/{textPartId}")
    public ResponseEntity<TextPart> findTextPartById(@PathVariable Long textPartId, /*@PathVariable Long commonDataId,*/ HttpServletRequest request)
            throws NoHandlerFoundException {
        TextPart textPart = textPartService.findTextPartById(textPartId)
                .orElseThrow(() ->
//                        new NoHandlerFoundException("GET", "/text-common-data/"+ commonDataId + "/text-parts/"
//                        + textPartId, new HttpHeaders()));
//                        new NoHandlerFoundException(request.getMethodValue(), request.getURI().getPath(), new HttpHeaders()));
                        new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), new HttpHeaders()));
        return ResponseEntity.ok().body(textPart);
    }

//    @GetMapping (path = "/text-parts/{id}")
//    public ResponseEntity<TextPart> getTextPartById() {
//
//    }

//        @GetMapping(value = "/text-parts", params = {"page"})
//        public ResponseEntity<TextPartPagedLinkedRepresentation> findPage(@RequestParam("page") int page) {
////            Page<TextPartPagedLinkedRepresentation> resultPage =
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
//// FIXME: 25.11.2018 нужно вернуть не firstTextPartId, а сущность Bookmark, вынуть из нее закладки, определить, какая страница пойдет и т.д.
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
//    public HttpEntity<TextPartPagedLinkedRepresentation> getLastOpenPageByTextCommonDataId(
//            @PathVariable long commonDataId,
//            HttpServletRequest request
//    ) {
//
//    }




// ================================================================================================================ GET:
//                                          fixme: open document, find page by document id (textCommonData id) and page number

    /** <b>Open document by its id (textCommonData id). First rendered page is specified by textNumber parameter</b>
     * <p>
     * Standard Spring Data JPA pagination assumes that each time the page is invoked, Hibernate makes three calls:
     * <p>
     * Hibernate: SELECT * FROM public.get_all_texparts_ordered_set(?) limit ? offset ?
     * <p>
     * Hibernate: SELECT count(*) FROM public.get_all_texparts_ordered_set(?)
     * <p>
     * Hibernate: SELECT * FROM public.find_textpart_by_id(?)
     * <p>
     * It makes sense in asynchronous application, when several users have access to the same db rows, and the
     * data base items could be changed between two page calls.
     * <p>
     * This application assumes that data base will be reformatted only after current document closing
     * or after pushing "save" button, and only one user has access to the same rows at the same time.
     * To avoid expensive data base requests, the relevant metadata should be saved in the special repository
     * of {@code TextPartControllerTransientDataRepo} type, and then controller uses it in pages calls */
    @GetMapping(value = "/text-parts/pages", params = {"page"})
    public HttpEntity<TextPartPagedLinkedRepresentation> findPageByTextCommonDataIdAndPageNumber(
            @PathVariable long commonDataId,
            @RequestParam("page") int pageNumber,
            HttpServletRequest request
    ) throws NoHandlerFoundException {
        if (!repository.isRunning()) {
                repository = TextPartControllerTransientDataRepo.builder()
                        .listOfSortedTextPartId(textPartService.findSortedTextPartIdByTextCommonDataId(commonDataId))
                        .isRunning(true)
                        .build();
                repository.setTotalPages(repository.getListOfSortedTextPartId().size());
        }
        repository.setPageNumber(pageNumber);
        TextPart textPart = textPartService
                // the page number must belong to the set {1; totalPages}
                    .findTextPartById(repository.getListOfSortedTextPartId().get(pageNumber - 1))
                    .orElseThrow(() -> new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), new HttpHeaders()));
//        System.out.println("*************************************************************************" + textPart);

        TextPartPagedLinkedRepresentation linkedPage;
        try {
//            System.out.println("********************************************" + repository.isRunning() +
//            "*********" + repository.getListOfSortedTextPartId() + "***************" + repository.getPageNumber() + "**" + repository.getTotalPages());
            linkedPage = assembler.getLinkedPage(textPart, commonDataId, pageNumber, request, repository);
//            System.out.println("*************************************************************************" + linkedPage);
        } catch (Exception e) {
            throw new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), new HttpHeaders());
        }
        return new ResponseEntity<>(linkedPage, HttpStatus.OK);
    }



    @PutMapping(value = "/text-parts/pages", params = {"page"})
    public HttpEntity<Date> updatePage(
            @RequestBody TextPartPagedLinkedRepresentation linkedPage,
            @RequestParam(value = "page") int pageNumber,
            HttpServletRequest request
    ) throws NoHandlerFoundException, ParseException {
        String body = linkedPage.getBody();
        Date current = textPartService.updateById( // FIXME: 17.11.2018 format date to UTC+2.00
                repository.getListOfSortedTextPartId().get(pageNumber - 1),
                body,
                new Date())
                .orElseThrow(() -> new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), new HttpHeaders()));
        return new ResponseEntity<>(current, new HttpHeaders(), HttpStatus.OK);
    }


//================================================================================================================ POST:
//                                                   create fully new textpart (or )

    @PostMapping(value = "/text-parts"/*, consumes = {MediaTypes.HAL_JSON_UTF8_VALUE, MediaTypes.HAL_JSON_VALUE}*/)
    public ResponseEntity<Void> createTextPart(
            /*@Valid */@RequestBody TextPartPagedLinkedRepresentation textPartPagedLinkedRepresentation,
            UriComponentsBuilder uriBuilder,
            BindingResult bindingResult) {
        // здесь с помощью статического метода Manager проверяем textPartPagedLinkedRepresentation на соответствие, ошибки и т.п.
        TextPart textPart = converter.convertToEntity(textPartPagedLinkedRepresentation);
        TextPart textPartCreated = textPartService.create(textPart);
//        TextPartPagedLinkedRepresentation textPartResourceCreated = textPartManager.convertToLinkedPage(textPartCreated);
//        HttpHeaders headers = new HttpHeaders();
//        URI uri = uriBuilder
//                        .port(port)
//                        .path("/" + env.getRequiredProperty("server.servlet.context-path") +
//                                "/text-common-data/{commonDataId}/text-parts/{textPartId}")
//                        .encode()
//                        .buildAndExpand(textPartCreated.getTextCommonData().getId(), textPartCreated.getId())
//                        .toUri();
//        headers.setLocation(uri);
//        return new ResponseEntity<TextPartPagedLinkedRepresentation>(textPartResourceCreated, headers, HttpStatus.CREATED);

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
