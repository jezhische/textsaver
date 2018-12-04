package com.jezh.textsaver.repository;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.Bookmark;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookmarkRepositoryPostgresTest extends BasePostgresConnectingTest {

    private Bookmark bookmark;

    private TextCommonData textCommonData;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TextCommonDataRepository textCommonDataRepository;

    @Before
    public void setUp() throws Exception {
//        textCommonData = textCommonDataRepository.findById(1L).get();
        textCommonData = textCommonDataRepository.findById(13L).get();
        bookmark = Bookmark.builder()
                .textCommonData(textCommonData)
                .lastOpenArray(TestUtil.createRandom10BookmarkDefsArray())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        textCommonData = null;
        bookmark = null;
    }

//    @Ignore
    @Test // try to save the new entity when another one with the same id exists: JpaSystemException ->
    // org.springframework.dao.DataIntegrityViolationException: A different object with the same identifier value was
    // already associated with the session
    public void create() {
//        int[] created = bookmark.getLastOpenArray();
        bookmarkRepository.saveAndFlush(bookmark);
//        assertNotNull(bookmarkRepository.findById(bookmark.getId()));
//        int[] saved = bookmarkRepository.findById(bookmark.getId()).get().getLastOpenArray();
//        assertEquals(created, saved);
//        System.out.println("********************************************** created: ");
//        for (int i : created) {
//            System.out.print(i + ", ");
//        }
//        System.out.println("********************************************** saved: ");
//        for (int i : saved) {
//            System.out.print(i + ", ");
//        }
    }



}
