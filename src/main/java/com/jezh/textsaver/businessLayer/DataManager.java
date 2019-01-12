package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.controller.BookmarksController;
import com.jezh.textsaver.controller.TextCommonDataController;
import com.jezh.textsaver.controller.TextPartController;
import com.jezh.textsaver.dto.BookmarksData;
import com.jezh.textsaver.dto.LRUCacheMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DataManager {

//    private TextCommonDataControllerTransientDataRepo repository;
//
//    private TextCommonDataResourceAssembler assembler;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT;

//    private Environment env;

    static {SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");}

    private final String port;

    private static final int BOOKMARKS_COUNT = 10;

    @Autowired
    public DataManager(Environment env) {
//        this.env = env;
        port = env.getRequiredProperty("local.server.port"); // by unknown reason property "server.port" in
        // spring boot 2 returns "-1", so this is a crutch;
    }

    // TODO: to remove?
    public static String getUniqueName(String docDataName, Date createdDate) {
        if (createdDate != null)
            return new StringBuilder()
                    .append(docDataName)
                    .append("-created-date-")
                    .append(SIMPLE_DATE_FORMAT.format(createdDate)).toString();
        else
            return docDataName;
    }

    public static String getLastOpenedArrayItem(int pageNumber, boolean isEdited) {
        return String.valueOf(pageNumber) + (isEdited ? "1" : "0");
    }

    /**
     *
     * */
    public String createPageLink(long docDataId, int pageNumber) throws NoHandlerFoundException, UnknownHostException {
//        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        URI resourcePath = linkTo(methodOn(TextPartController.class)
                .findPage(docDataId, pageNumber)).toUri();
        String uri = UriComponentsBuilder.newInstance().scheme("http")
                .host(/*hostAddress*/"localhost").port(port)
//                .path(env.getRequiredProperty("server.servlet.context-path")) // by unknown reason, from test the
//                context-path isn't created, and this line is necessary for test, but from controller this line
//                causes duplication of context-path, so I need to remove it for application run
                .path(resourcePath.getPath())
                .query(resourcePath.getQuery())
                .toUriString();
          return uri; // http://localhost:port/textsaver/doc-data/docDataId/pages?page=pageNumber
    }


    public String createBookmarksLink(long docDataId, BookmarksData bookmarksData) throws NoHandlerFoundException, UnknownHostException {
        URI resourcePath = linkTo(methodOn(BookmarksController.class).getBookmarks(docDataId, bookmarksData)).toUri();
        return UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost").port(port)
                .path(resourcePath.getPath())
                .toUriString(); // http://localhost:port/textsaver/doc-data/bookmarks
    }


    public static String trimQuotes(String obtainedName) {
        if (obtainedName.startsWith("\"") || obtainedName.startsWith("'") ||
                obtainedName.endsWith("\"") || obtainedName.endsWith("'"))
            obtainedName = obtainedName.substring(1, obtainedName.length() - 1);
        return obtainedName;
    }

    public String[] updateLastOpenArray(String[] lastOpenArray, int previousPageNumber, boolean isPageUpdated, int totalPages) {
        if (previousPageNumber > totalPages -1) return lastOpenArray;
        LRUCacheMap<String, String> cacheMap = new LRUCacheMap<>(BOOKMARKS_COUNT);
        for (String item : lastOpenArray) {
            int length = item.length();
            cacheMap.put(item.substring(0, length - 1), item.substring(length - 1));
        }
        cacheMap.put(String.valueOf(previousPageNumber), isPageUpdated ? "1" : "0");
        String[] updated = new String[cacheMap.size()];
        Set<Map.Entry<String, String>> entries = cacheMap.entrySet();
        List<String> strings = new LinkedList<>();
        entries.forEach(entry -> strings.add(entry.getKey() + entry.getValue()));
        return strings.toArray(updated);
    }

    public int[] updateSpecialBookmarks(int[] specialBookmarks, int previousPageNumber, int totalPages) {
        if (previousPageNumber > totalPages -1) return specialBookmarks;
        TreeSet<Integer> specials = new TreeSet<>();
        if (specialBookmarks != null && specialBookmarks.length != 0) {
            for (int specialBookmark : specialBookmarks) {
                specials.add(specialBookmark);
            }
        }
        specials.add(previousPageNumber);
        int[] ints = new int[specials.size()];
        LinkedList<Integer> list = new LinkedList<>(specials);
        for (int i = 0; i < list.size(); i++) {
            ints[i] = list.get(i);
        }
        return ints;
    }
}
