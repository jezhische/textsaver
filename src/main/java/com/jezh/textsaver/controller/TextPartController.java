package com.jezh.textsaver.controller;

import com.jezh.textsaver.dto.TextPartDTO;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.TextCommonDataService;
import com.jezh.textsaver.service.TextPartService;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/text-common-data/{commonDataId}")
public class TextPartController {

    @Autowired
    private TextPartService textPartService;

    @Autowired
    private TextCommonDataService textCommonDataService;

    @Autowired
    Environment env;

//    @LocalServerPort
//    private int port;

    @Autowired
    private MapperFactory mapperFactory;

    @PostMapping(path = "/text-parts")
//    @ResponseStatus(HttpStatus.CREATED)  // for update HttpStatus.OK
    public ResponseEntity<TextPartDTO> createTextPart(@Valid @RequestBody TextPartDTO textPartDTO,
                                                      UriComponentsBuilder uriBuilder,
                                                      BindingResult bindingResult) {
        // здесь с помощью статического метода Manager проверяем textPartDTO на соответствие, ошибки и т.п.
        TextPart textPart = convertToEntity(textPartDTO);
        TextPart textPartCreated = textPartService.create(textPart);
        TextPartDTO textPartDTOCreated = convertToDTO(textPartCreated);
        HttpHeaders headers = new HttpHeaders();
        URI uri = /*UriComponentsBuilder.newInstance()*/
                uriBuilder
//                .scheme("http")
//                .host("localhost")
                .port(env.getRequiredProperty("local.server.port"))
                .path("/" + env.getRequiredProperty("server.servlet.context-path") +
                        "/text-common-data/{commonDataId}/text-parts/{textPartId}")
//                .encode()
                .buildAndExpand(textPartDTOCreated.getTextCommonData().getId(), textPartDTOCreated.getId())
                .toUri();
//                URI uri = ServletUriComponentsBuilder
//                        .fromCurrentRequest()
//                        .path("/{textPartId}")
//                        .encode()
//                        .buildAndExpand(textPartDTOCreated.getTextCommonData().getId(), textPartDTOCreated.getId())
//                        .toUri();
        headers.setLocation(uri);
        return new ResponseEntity<TextPartDTO>(textPartDTOCreated, headers, HttpStatus.CREATED);
    }

    /* find all the textParts with given textCommonData id */
//    @GetMapping (path = "/text-parts")
//    public ResponseEntity<?> getTextPartListByTextCommonDataId(@PathVariable("commonDataId") Long textCommonDataId) {
//        return ResponseEntity.ok().body(textPartService.findByTextCommonDataId(textCommonDataId));
//    }

    /** find textPart by id */
    @GetMapping (path = "/text-parts/{textPartId}")
    public ResponseEntity<TextPart> findTextPartById(@PathVariable Long textPartId) {
        TextPart textPart = null;
//        try {
        textPart = textPartService.findTextPartById(textPartId)
                .get();
//                .orElseThrow(() -> new ResNotFoundException("textPart with such id is not found", new SQLException()));
//        } catch (ResNotFoundException ex) {return ResponseEntity.notFound().build();}
        return ResponseEntity.ok().body(textPart);
    }

//    @GetMapping(path = "/text-parts/")

//    @GetMapping (path = "/text-parts/{id}")
//    public ResponseEntity<TextPart> getTextPartById() {
//
//    }

    /**
     * find a bunch of textPart in the sorted order start from the given textPart id and with the given size.
     * If request parameter is not specified, find all the textPart with given textCommonDataId in this order
     */
    @GetMapping(path = "/text-parts") // FIXME: 24.10.2018 add (NullPointer) exceptions handling
    public ResponseEntity<List<TextPart>> findSortedTextPartBunchByStartId(
            @PathVariable("commonDataId") Long textCommonDataId,
            @RequestParam(value = "startId", required = false) Long startId,
            @RequestParam(value = "size", required = false) Integer size) { // not int or long, otherwise I get "IllegalStateException:
        // Optional int parameter 'size' is present but cannot be translated into a null value due to being declared as a primitive type."
        if (startId == null && size == null) {
        return ResponseEntity.ok().body(textPartService.findSortedSetByTextCommonDataId(textCommonDataId));
        } else if (startId == null && size != 0) {
            Long firstTextPartId = textCommonDataService.findTextCommonDataById(textCommonDataId).get().getFirstItem();
            return ResponseEntity.ok().body(textPartService.findSortedTextPartBunchByStartId(firstTextPartId, size));
        } else {
            return ResponseEntity.ok().body(textPartService.findSortedTextPartBunchByStartId(startId, size));
        }
    }

// ================================================================================================= UTILITY

    private TextPartDTO convertToDTO(TextPart textPart) {
        return mapperFactory.getMapperFacade(TextPart.class, TextPartDTO.class).map(textPart);
    }

    private TextPart convertToEntity(TextPartDTO textPartDTO) {
        return mapperFactory.getMapperFacade(TextPart.class, TextPartDTO.class).mapReverse(textPartDTO);
    }
}
