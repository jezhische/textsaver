package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.dto.BookmarkResource;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.service.BookmarkService;
import com.jezh.textsaver.service.TextPartService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.UnknownHostException;
import java.util.List;

import static org.junit.Assert.*;

public class TextPartResourceAssemblerTest extends BasePostgresConnectingTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private TextPartService textPartService;

    @Autowired
    TextPartResourceAssembler assembler;

    private Bookmarks bookmarks;

    @Before
    public void setUp() throws Exception {
        bookmarks = bookmarkService.findById(14L).get();
    }

    @After
    public void tearDown() throws Exception {
        bookmarks = null;
    }


    @Test
    public void getBookmarkResourceList() throws NoHandlerFoundException, UnknownHostException {
        List<BookmarkResource> bookmarkResourceList = assembler.convertBookmarksToBookmarkResourceList(bookmarkService.findById(31L).get());
        bookmarkResourceList.forEach(System.out::println);
        assertEquals("1b5412", bookmarkResourceList.get(0).getColor());

        bookmarkResourceList = assembler.convertBookmarksToBookmarkResourceList(bookmarkService.findById(33L).get());
        bookmarkResourceList.forEach(System.out::println);
        assertEquals("ff3300", bookmarkResourceList.get(0).getColor());
    }

    @Test
    public void addElsePagesLinks() throws NoHandlerFoundException, UnknownHostException {
        List<BookmarkResource> bookmarkResourceList = assembler.convertBookmarksToBookmarkResourceList(bookmarkService.findById(31L).get());
        System.out.println("**************************************************************************************");
        bookmarkResourceList.forEach(System.out::println);
        bookmarkResourceList = assembler.addElsePagesLinks(bookmarkResourceList,
                textPartService.findPageByDocDataIdAndPageNumber(31L, 1), 31L);
        System.out.println("**************************************************************************************");
        bookmarkResourceList.forEach(System.out::println);
    }
}