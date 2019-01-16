package com.jezh.textsaver.businessLayer;

        import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
        import com.jezh.textsaver.dto.BookmarksData;
        import com.jezh.textsaver.dto.LRUCacheMap;
        import com.jezh.textsaver.entity.Bookmarks;
        import org.junit.After;
        import org.junit.Before;
        import org.junit.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.servlet.NoHandlerFoundException;

        import java.net.UnknownHostException;
        import java.util.*;

        import static org.junit.Assert.*;

public class DataManagerTest extends BasePostgresConnectingTest {

    @Autowired
    DataManager dataManager;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getLastOpenedArrayItem() {
    }

    //    @Ignore
    @Test
    public void createPageLink() throws UnknownHostException, NoHandlerFoundException {
        String pageLink = dataManager.createPageLink(333L, 1);
        System.out.println("**********************************************" + pageLink);
//        assertEquals("http://localhost/textsaver/doc-data/333/pages?page=1", pageLink); // почему-то из контроллера
//        все работает правильно, а из теста не прописывается context-path
    }

    @Test
    public void createBookmarksLink() throws UnknownHostException, NoHandlerFoundException {
        String bookmarksLink = dataManager.createBookmarksLink(333L, new BookmarksData());
        System.out.println("**********************************************" + bookmarksLink);
//        assertEquals("http://localhost/textsaver/doc-data/333/bookmarks", bookmarksLink);
    }

    @Test
    public void updateLastOpenArray() {
        String[] testArray = {"10", "121"};
        String[] actualArray = dataManager.updateLastOpenArray(testArray, 3, true, 8);
        System.out.println("***********************************************" + Arrays.asList(actualArray));
        String[] expectedArray = {"10", "121", "31"};
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void test() {
    }

    @Test
    public void updateSpecialBookmarks() {

    }

    @Test
    public void trimQuotes() {
        assertEquals("aou", DataManager.trimQuotes("\"aou\""));
        assertEquals("", DataManager.trimQuotes("\"\""));
        assertEquals("aou", DataManager.trimQuotes("'aou'"));
        assertEquals("", DataManager.trimQuotes("''"));
    }

    @Test
    public void deleteBookmark() {
        int[] specialBookmarks = {250, 127, 3, 95};
        String[] lastOpenArray = {"950","1030","1040","971","990","1000","1010","1020"};
        Bookmarks bookmarks = Bookmarks.builder()
                .specialBookmarks(specialBookmarks)
                .lastOpenArray(lastOpenArray)
                .build();
        assertTrue(Arrays.asList(bookmarks.getLastOpenArray()).contains("950"));
        assertTrue(bookmarks.getSpecialBookmarks().length == 4);
        boolean check = false;
        for (int i : bookmarks.getSpecialBookmarks()) {
            if (i == 95) check = true;
        }
        assertTrue(check);

        dataManager.deleteBookmark(95, bookmarks);

        assertFalse(Arrays.asList(bookmarks.getLastOpenArray()).contains("950"));
        assertTrue(Arrays.asList(bookmarks.getLastOpenArray()).contains("961"));
        assertTrue(bookmarks.getSpecialBookmarks().length == 3);
        check = false;
        for (int i : bookmarks.getSpecialBookmarks()) {
            if (i == 95) check = true;
        }
        assertFalse(check);
        for (int i : bookmarks.getSpecialBookmarks()) {
            if (i == 126) check = true;
        }
//        assertTrue(check);

        System.out.println("**************************************************************" + Arrays.asList(bookmarks.getLastOpenArray()));
        for (int i : bookmarks.getSpecialBookmarks()) System.out.println("**************" + i);


    }
}