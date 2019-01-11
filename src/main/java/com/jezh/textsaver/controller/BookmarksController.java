package com.jezh.textsaver.controller;

import com.jezh.textsaver.businessLayer.BookmarkResourceAssembler;
import com.jezh.textsaver.businessLayer.DataManager;
import com.jezh.textsaver.dto.BookmarkResource;
import com.jezh.textsaver.dto.BookmarksData;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
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
                                                               @RequestBody BookmarksData bookmarksData)
            throws NoHandlerFoundException, UnknownHostException {
        String url = dataManager.createBookmarksLink(id, bookmarksData);
        Bookmarks bookmarks = bookmarkService.findById(id).orElseThrow(
                () -> new NoHandlerFoundException("GET", url, new HttpHeaders()));
        return assembler.convertBookmarksToBookmarkResourceList(bookmarks, bookmarksData.getCurrentPageNumber(), bookmarksData.getTotalPages());
    }

    @PutMapping(value = "/bookmarks")
    public List<BookmarkResource> updateBookmarks(@PathVariable(value = "commonDataId") long id,
                                               @RequestBody BookmarksData bookmarksData)
            throws NoHandlerFoundException, UnknownHostException {
        String url = dataManager.createBookmarksLink(id, bookmarksData);
        Bookmarks bookmarks = bookmarkService.findById(id).orElseThrow(
                () -> new NoHandlerFoundException("GET", url, new HttpHeaders()));
        System.out.println("***********************************************************" + bookmarksData);
        int previousPageNumber = bookmarksData.getPreviousPageNumber();
            bookmarks.setLastOpenArray(dataManager.updateLastOpenArray(bookmarks.getLastOpenArray(), previousPageNumber,
                    bookmarksData.isPageUpdated()));
            bookmarks.setSpecialBookmarks(dataManager.updateSpecialBookmarks(bookmarks.getSpecialBookmarks(), // FIXME: 09.01.2019 fix updateSpecialBookmarks()
                    previousPageNumber, bookmarksData.isSpecialBookmark()));
        System.out.println("***********************************************************" + bookmarks);
        bookmarkService.update(bookmarks);
        List<BookmarkResource> bookmarkResources = assembler.convertBookmarksToBookmarkResourceList(bookmarks, bookmarksData.getCurrentPageNumber(),
                bookmarksData.getTotalPages());
        bookmarkResources.forEach(bookmarkResource -> System.out.println("************* bookmarkResource:" + bookmarkResource));
        return bookmarkResources;
    }


}
