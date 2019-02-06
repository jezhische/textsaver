package com.jezh.textsaver.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarksData {
    private int previousPageNumber;
    private int currentPageNumber;
    private int totalPages;
    private boolean isPageUpdated; // lombok library requires names for booleans like "isPageUpdated" instead of "isPageUpdated",
    // and then it creates getter as "isPageUpdated". If I make the field username as "isPageUpdated", I've got the additional
    // fields for object (e.g. both fields "isPageUpdated" and "isPageUpdated" when the client returns the bookmarksData object),
    // but I need no such additives
    private boolean isSpecialBookmark;


}
