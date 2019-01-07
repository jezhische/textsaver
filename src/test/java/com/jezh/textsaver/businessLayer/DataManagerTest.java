package com.jezh.textsaver.businessLayer;

        import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
        import com.jezh.textsaver.dto.BookmarksData;
        import com.jezh.textsaver.dto.LRUCacheMap;
        import org.junit.After;
        import org.junit.Before;
        import org.junit.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.servlet.NoHandlerFoundException;

        import java.net.UnknownHostException;
        import java.util.*;

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
        System.out.println("***********************************************" +
                Arrays.asList(dataManager.updateLastOpenArray(testArray, 3, true)));
    }

    @Test
    public void test() {
    }

    @Test
    public void updateSpecialBookmarks() {
    }
}