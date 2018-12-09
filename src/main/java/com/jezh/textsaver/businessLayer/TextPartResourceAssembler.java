package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.dto.BookmarkResource;
import com.jezh.textsaver.dto.TextPartResource;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.entity.entityProperties.EditedColorStore;
import com.jezh.textsaver.entity.entityProperties.OpenedColorStore;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class TextPartResourceAssembler {

    private final static String adjasentPageReferencesColor = "ffffff";

    private final static String currentPageReferenceColor = "fee901";

    /**
     * supply TextPartResources with links to current, adjacent, first and last pages and last opened/edited pages
     * */
    public TextPartResource getResource(Page<TextPart> currentPage, Bookmarks bookmarks)
            throws NullPointerException, NoHandlerFoundException {
        List<BookmarkResource> bookmarkResources = addElsePagesLinks(getBookmarkResourceList(bookmarks),
                currentPage, bookmarks.getId());
        return convertPageToMarkedResource(currentPage, bookmarkResources);
    }




// ======================================================================================================= Util


    private TextPartResource convertPageToMarkedResource(Page<TextPart> currentPage, List<BookmarkResource> bookmarkResources) {
        TextPart textPart = currentPage.getContent().stream().findFirst()
                .orElseThrow(NullPointerException::new);
        String body = textPart.getBody();
        Date lastUpdate = textPart.getLastUpdate();
        int pageNumber = currentPage.getNumber() + 1;
        int totalPages = currentPage.getTotalPages();
        return TextPartResource.builder()
                .body(body)
                .lastUpdate(lastUpdate)
                .pageNumber(pageNumber)
                .totalPages(totalPages)
                .bookmarkResources(bookmarkResources)
                .build();
    }


    // FIXME: 08.12.2018 пробросить нужные исключения
    /**
     * Returns list of BookmarkResource as they coded in the appropriate Bookmarks item (with the Bookmarks references only)
     * */
     List<BookmarkResource> getBookmarkResourceList(Bookmarks bookmarks)
            throws NoHandlerFoundException {
        String[] lastOpenArray = bookmarks.getLastOpenArray();
        EditedColorStore[] editedColorStores = EditedColorStore.values();
        OpenedColorStore[] openedColorStores = OpenedColorStore.values();
        long docDataId = bookmarks.getId();
        List<BookmarkResource> resources = new LinkedList<>();
        for (int i = 0; i < lastOpenArray.length; i++) {
            String current = lastOpenArray[i];
            if (current != null) {
                // NumberFormatException
                int pageNumber = Integer.parseInt(current.substring(0, current.length() - 1));
                // set the color according to page index in lastOpenArray and isEdited value
                String color = current.endsWith("1") ?
                        editedColorStores[i].name().substring(1) :
                        openedColorStores[i].name().substring(1);
                String link = DataManager.createPageLink(docDataId, pageNumber);
                resources.add(BookmarkResource.builder()
                        .pageNumber(pageNumber)
                        .color(color)
                        .link(link)
                        .build());
            }
        }
        return resources;
    }


    /**
     * add the new references to first, previous, current and adjacent pages to list with the Bookmarks references
     * */
    List<BookmarkResource> addElsePagesLinks(List<BookmarkResource> rawList, Page<TextPart> currentPage, long docDataId)
            throws NoHandlerFoundException {
         int totalPages = currentPage.getTotalPages();
// TODO NB: when current page number is 1, currentPage.getNumber() returns 0!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         int pageNumber = currentPage.getNumber() + 1;
         // simple way to sort page references in the natural order
        Map<Integer, BookmarkResource> rawMap = new TreeMap<>();
        rawList.forEach(bookmarkResource -> rawMap.put(bookmarkResource.getPageNumber(), bookmarkResource));
        // add adjacent page references to sorted map
        List<Integer> adjacentPages = new LinkedList<>();
        for (int i = pageNumber - 5; i < pageNumber + 5; i++) {
            if (i >= 1 && i <= totalPages && i != pageNumber && !rawMap.containsKey(i)) {
                String link = DataManager.createPageLink(docDataId, i);
                rawMap.put(i, BookmarkResource.builder()
                        .pageNumber(i)
                        .color(adjasentPageReferencesColor)
                        .link(link)
                        .build());
            }
        }
        // add the current page reference of yellow color
        rawMap.put(pageNumber, BookmarkResource.builder()
                .pageNumber(pageNumber)
                .color(currentPageReferenceColor)
                .link(DataManager.createPageLink(docDataId, pageNumber))
                .build());
        // add the last page reference, if not exists
        if (totalPages > 6 && !rawMap.containsKey(totalPages))
            rawMap.put(totalPages, BookmarkResource.builder()
                    .pageNumber(totalPages)
                    .color(adjasentPageReferencesColor)
                    .link(DataManager.createPageLink(docDataId, totalPages))
                    .build());
        // add the first page reference, if not exists
        if (!rawMap.containsKey(1))
            rawMap.put(1, BookmarkResource.builder()
                    .pageNumber(1)
                    .color(adjasentPageReferencesColor)
                    .link(DataManager.createPageLink(docDataId, 1))
                    .build());

        return new LinkedList<>(rawMap.values());
    }
}
