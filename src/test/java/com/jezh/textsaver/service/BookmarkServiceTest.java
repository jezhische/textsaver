package com.jezh.textsaver.service;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.Bookmark;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.repository.BookmarkRepository;
import com.jezh.textsaver.repository.TextCommonDataRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BookmarkServiceTest extends BasePostgresConnectingTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TextCommonDataRepository textCommonDataRepository;

    @Autowired
    private BookmarkService bookmarkService;

    private Bookmark bookmark;

    @Before
    public void setUp() throws Exception {
        bookmark = Bookmark.builder().textCommonData(textCommonDataRepository.getOne(36L)).build();
    }

    @After
    public void tearDown() throws Exception {
        bookmark = null;
    }


}
