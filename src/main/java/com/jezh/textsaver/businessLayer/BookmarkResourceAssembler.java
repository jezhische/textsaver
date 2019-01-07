package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.dto.BookmarkResource;
import com.jezh.textsaver.dto.BookmarksData;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.entity.entityProperties.EditedColorStore;
import com.jezh.textsaver.entity.entityProperties.OpenedColorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.UnknownHostException;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class BookmarkResourceAssembler {

    private final static String ADJASENT_PAGE_REFERENCES_COLOR = "rgb(246, 244, 246)"; // light grey

    private final static String CURRENT_PAGE_REFERENCE_COLOR = "ffdf00"; // yellow

    private final static String SPECIAL_BOOKMARK_COLOR = "00f6eb"; // blue

    private DataManager dataManager;

    private final EditedColorStore[] editedColorStores;
    private final OpenedColorStore[] openedColorStores;

    @Autowired
    public BookmarkResourceAssembler(DataManager dataManager) {
        this.dataManager = dataManager;
        editedColorStores = EditedColorStore.values();
        openedColorStores = OpenedColorStore.values();
    }


    // FIXME: 08.12.2018 пробросить нужные исключения
    /**
     * Returns list of BookmarkResource as they coded in the appropriate Bookmarks item (with the Bookmarks references only)
     * */
     public List<BookmarkResource> convertBookmarksToBookmarkResourceList(Bookmarks bookmarks,
                                                                          int pageNumber, int totalPages)
             throws NoHandlerFoundException, UnknownHostException {
        String[] lastOpenArray = bookmarks.getLastOpenArray(); // Strings kind of "page number + 1 or 0" (edited or opened)
        long docDataId = bookmarks.getId();
        List<BookmarkResource> resources = new LinkedList<>();
        for (int i = 0; i < lastOpenArray.length; i++) {
            String current = lastOpenArray[i];
            if (current != null) {
                // NumberFormatException
                int curPageNm = Integer.parseInt(current.substring(0, current.length() - 1));
                // set the color according to page index in lastOpenArray and isEdited value
                // (that means page index in lastOpenArray define the "least recent updated" option and,
                // accordingly  to it, the color of the page number button)
                String color = current.endsWith("1") ?
                        editedColorStores[i].name().substring(1) : // need to trim first letter
                        openedColorStores[i].name().substring(1);
                String currentPageLink = dataManager.createPageLink(docDataId, curPageNm + 1);
                String selfLink = dataManager.createBookmarksLink(docDataId, new BookmarksData());
                BookmarkResource bookmarkResource = BookmarkResource.builder()
                        .pageNumber(curPageNm + 1)
                        .color(color)
                        .pageLink(currentPageLink)
                        .build();
                bookmarkResource.add(new Link(selfLink).withSelfRel());
                resources.add(bookmarkResource);
            }
        }
        return addElsePagesLinks(resources, bookmarks, docDataId, pageNumber + 1, totalPages);
    }


    /**
     * add the new references to first, previous, current, adjacent and special pages to list with the Bookmarks references
     * */
    private List<BookmarkResource> addElsePagesLinks(List<BookmarkResource> rawList, Bookmarks bookmarks,
                                                     long docDataId, int pageNumber, int totalPages)
            throws NoHandlerFoundException, UnknownHostException {
//         int totalPages = currentPage.getTotalPages();
// TODO NB: when current page number is 1, currentPage.getNumber() returns 0!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         int currentPageNumber = currentPage.getNumber() + 1;
         // simple way to sort page references in the natural order
        Map<Integer, BookmarkResource> rawMap = new TreeMap<>();

        // add adjacent page references to sorted map (by 5 references right and left)
        for (int i = pageNumber - 5; i < pageNumber + 5; i++) {
            if (i >= 1 && i <= totalPages && i != pageNumber && !rawMap.containsKey(i)) {
                String link = dataManager.createPageLink(docDataId, i);
                rawMap.put(i, BookmarkResource.builder()
                        .pageNumber(i)
                        .color(ADJASENT_PAGE_REFERENCES_COLOR)
                        .pageLink(link)
                        .build());
            }
        }
        // add the last page reference, if not exists
        if (totalPages > 6 && !rawMap.containsKey(totalPages))
            rawMap.put(totalPages, BookmarkResource.builder()
                    .pageNumber(totalPages)
                    .color(ADJASENT_PAGE_REFERENCES_COLOR)
                    .pageLink(dataManager.createPageLink(docDataId, totalPages))
                    .build());
        // add the first page reference, if not exists
        if (!rawMap.containsKey(1))
            rawMap.put(1, BookmarkResource.builder()
                    .pageNumber(1)
                    .color(ADJASENT_PAGE_REFERENCES_COLOR)
                    .pageLink(dataManager.createPageLink(docDataId, 1))
                    .build());

        // add last opened/edited bookmarks
        rawList.forEach(bookmarkResource -> rawMap.put(bookmarkResource.getPageNumber(), bookmarkResource));

        // add special bookmarks (specialBookmarks array is the numbers of bookmarked pages)
        int[] specialBookmarks = bookmarks.getSpecialBookmarks();
        if (specialBookmarks != null && specialBookmarks.length != 0) {
            for (int bookmarkedPage : specialBookmarks) {
                String link = dataManager.createPageLink(docDataId, bookmarkedPage);
                rawMap.put(bookmarkedPage, BookmarkResource.builder()
                        .pageNumber(bookmarkedPage)
                        .color(SPECIAL_BOOKMARK_COLOR)
                        .pageLink(link)
                        .build());
            }
        }

        // add the current page reference of yellow color
        rawMap.put(pageNumber, BookmarkResource.builder()
                .pageNumber(pageNumber)
                .color(CURRENT_PAGE_REFERENCE_COLOR)
                .pageLink(dataManager.createPageLink(docDataId, pageNumber))
                .build());

        return new LinkedList<>(rawMap.values());
    }
}
