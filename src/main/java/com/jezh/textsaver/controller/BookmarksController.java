package com.jezh.textsaver.controller;

import com.jezh.textsaver.businessLayer.BookmarkResourceAssembler;
import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.dto.BookmarkResource;
import com.jezh.textsaver.dto.BookmarksAux;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc-data/{commonDataId}")
public class BookmarksController {

    private final BookmarkResourceAssembler assembler;
    private final BookmarkService bookmarkService;
    private final DataManager dataManager;


    @Autowired
    public BookmarksController(BookmarkResourceAssembler assembler, BookmarkService bookmarkService, DataManager dataManager) {
        this.assembler = assembler;
        this.bookmarkService = bookmarkService;
        this.dataManager = dataManager;
    }

//    @GetMapping("/bookmarks")
//    public ResponseEntity<?> getBookmarks(@PathVariable(value = "commonDataId") long id)
//            throws NoHandlerFoundException, UnknownHostException {
//        String url = dataManager.createBookmarksLink(id);
//        Bookmarks bookmarks = bookmarkService.findById(id).orElseThrow(
//                () -> new NoHandlerFoundException("GET", url, new HttpHeaders()));
//        return ResponseEntity.ok(assembler.convertBookmarksToBookmarkResourceList(bookmarks));
//    }

    @PostMapping(value = "/bookmarks")
    public List<BookmarkResource> getBookmarks(@PathVariable(value = "commonDataId") long id,
                                                               @RequestBody BookmarksAux aux)
            throws NoHandlerFoundException, UnknownHostException {
        String url = dataManager.createBookmarksLink(id, aux);
        Bookmarks bookmarks = bookmarkService.findById(id).orElseThrow(
                () -> new NoHandlerFoundException("GET", url, new HttpHeaders()));
        System.out.println("************************************************************************** " + aux.getPageNumber() + ", " + aux.getTotalPages());
//        return assembler.convertBookmarksToBookmarkResourceList(bookmarks, aux.getPageNumber(), aux.getTotalPages());
        BookmarkResource bookmarkResource = BookmarkResource.builder().color("a").pageLink("b").pageNumber(5).build();
        return Arrays.asList(bookmarkResource);
    }


}
