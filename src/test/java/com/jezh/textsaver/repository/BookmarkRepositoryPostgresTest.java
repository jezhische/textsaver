package com.jezh.textsaver.repository;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.Bookmark;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BookmarkRepositoryPostgresTest extends BasePostgresConnectingTest {

    private Bookmark bookmark;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TextCommonDataRepository textCommonDataRepository;

    @Before
    public void setUp() throws Exception {
        bookmark = Bookmark.builder().textCommonData(textCommonDataRepository.getOne(36L)).build();
    }

    @After
    public void tearDown() throws Exception {
        bookmark = null;
    }

    @Test
    public void create() {
        bookmarkRepository.saveAndFlush(bookmark);
        assertNotNull(bookmarkRepository.findById(bookmark.getId()));
    }

    @Test
    public void testGetAllInSortedOrder() {
        List<Bookmark> list = bookmarkRepository.getAllInSortedOrder(36L);
        list.forEach(System.out::println);
    }
}
