package com.jezh.textsaver.controller;

import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.businessLayer.PageResourceAssembler;
import com.jezh.textsaver.dto.PageResource;
import com.jezh.textsaver.dto.TextPartResource;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.service.BookmarkService;
import com.jezh.textsaver.service.TextPartService;
import com.jezh.textsaver.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Date;

@RestController
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
@RequestMapping("/doc-data/{commonDataId}")
public class TextPartController {

    private final TextPartService textPartService;

    private final PageResourceAssembler pageModelAssembler;

    private final DataManager dataManager;

    // the same as @Value("${local.server.port}")
//    @LocalServerPort
//    private int port;

    @Autowired
    public TextPartController(PageResourceAssembler pageModelAssembler, DataManager dataManager,
                              TextPartService textPartService) {
        this.pageModelAssembler = pageModelAssembler;
        this.dataManager = dataManager;
        this.textPartService = textPartService;
    }


// ================================================================================================================ GET:
    /** find page model by textCommonData id and page number */
    @GetMapping(value = "/pages", params = {"page"})
    public ResponseEntity<PageResource> findPage(@PathVariable(value = "commonDataId") long id,
                                                 @RequestParam(value = "page") int pageNumber)
            throws NoHandlerFoundException, UnknownHostException {
        Page<TextPart> page = textPartService.findPageByDocDataIdAndPageNumber(id, pageNumber);
        return ResponseEntity.status(HttpStatus.OK).body(pageModelAssembler.getResource(page));
    }

// ================================================================================================================ POST:

    /**
     * insert new page after current one
     * */
    @PostMapping(value = "/pages", params = {"page"})
    public HttpEntity<PageResource> insertPage(@PathVariable(value = "commonDataId") long docDataId,
                                                   @RequestParam(value = "page") int newPageNumber)
            throws NoHandlerFoundException, UnknownHostException {
        Page<TextPart> newPage = textPartService.createPage(newPageNumber, docDataId);
        return ResponseEntity.status(HttpStatus.CREATED).body(pageModelAssembler.getResource(newPage));
    }

// ================================================================================================================ PUT:

    /**
     * update current page
     * */
    @PutMapping(value = "/pages", params = {"page"})
    public void updatePage(@PathVariable(value = "commonDataId") long docDataId,
                                                   @RequestParam(value = "page") int currentPage,
                                                   @RequestBody String pageContent)
            throws NoHandlerFoundException, UnknownHostException {
        String url = dataManager.createPageLink(docDataId, currentPage);
        TextPart textPart = textPartService.findPageByDocDataIdAndPageNumber(docDataId, currentPage)
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoHandlerFoundException("put", url, new HttpHeaders()));
//        textPart.setBody(pageContent);
//        textPartService.update(textPart);
        Date updated = new Date();
        textPart.getTextCommonData().setUpdatedDate(updated);
        long textPartId = textPart.getId();
        textPartService.updateById(textPartId, DataManager.trimQuotes(pageContent), updated);
    }


// ============================================================================================================ DELETE:
    @DeleteMapping(value = "/pages", params = {"page"})
    public void delete(@PathVariable(value = "commonDataId") long docDataId,
                       @RequestParam(value = "page") int pageNm) throws UnknownHostException, NoHandlerFoundException {
        if (pageNm == 0) return;
        textPartService.delete(docDataId, pageNm);

        // TODO: НУЖНО ЕЩЕ СДВИНУТЬ НОМЕРА СТРАНИЦ В ЗАКЛАДКАХ (В МАССИВАХ) НА -1, ЧТОБЫ ОНИ СОХРАНИЛИСЬ НА СДВИНУТЫХ ВЛЕВО
        //  НОМЕРАХ СТРАНИЦ И НЕ УКАЗЫВАЛИ НА УЖЕ НЕСУЩЕСТВУЮЩУЮ ПОСЛЕДНЮЮ СТРАНИЦУ
        //  ТО ЖЕ САМОЕ ДЛЯ ИНСЕРТ СТРАНИЦЫ!
    }
//

}
