package com.jezh.textsaver.service;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.repository.BookmarkRepository;
import com.jezh.textsaver.repository.TextCommonDataRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BookmarksServiceTest extends BasePostgresConnectingTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TextCommonDataRepository textCommonDataRepository;

    @Autowired
    private BookmarkService bookmarkService;

    private Bookmarks bookmarks;

    @Before
    public void setUp() throws Exception {
        bookmarks = Bookmarks.builder().textCommonData(textCommonDataRepository.getOne(1L)).build();
    }

    @After
    public void tearDown() throws Exception {
        bookmarks = null;
    }

    @Test
    public void testFindById() {
        Bookmarks bookmarks = bookmarkRepository.findById(1795L).get();
        System.out.println("******************************************************* " + bookmarks);
        assertTrue(bookmarks.getLastOpenArray()[1] == null);
    }
}
