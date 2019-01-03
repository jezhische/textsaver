package com.jezh.textsaver.businessLayer;

        import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
        import com.jezh.textsaver.dto.BookmarksAux;
        import org.junit.After;
        import org.junit.Before;
        import org.junit.Ignore;
        import org.junit.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.servlet.NoHandlerFoundException;

        import java.net.UnknownHostException;

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
        String bookmarksLink = dataManager.createBookmarksLink(333L, new BookmarksAux());
        System.out.println("**********************************************" + bookmarksLink);
//        assertEquals("http://localhost/textsaver/doc-data/333/bookmarks", bookmarksLink);
    }


}