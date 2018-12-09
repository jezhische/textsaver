package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.Bookmark;
import com.jezh.textsaver.dto.BookmarkDef;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class DocumentDataRepository {

    public static TextCommonData createTextCommonData(String name) {
        return TextCommonData.builder()
                .name(name)
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();
    }

    public static TextPart createTextPart(TextCommonData textCommonData) {
        TextPart textPart = TextPart.builder().lastUpdate(new Date()).build();
        textPart.setTextCommonData(textCommonData);
        return textPart;
    }

    public static Bookmark createBookmark(int bookmarksCount, TextCommonData textCommonData) {
        String[] lastOpens = new String[bookmarksCount];
        BookmarkDef bookmarkDef = BookmarkDef.builder().page_number(1).build();
        lastOpens[0] = bookmarkDef.toString();
        Bookmark bookmark = Bookmark.builder().lastOpenArray(lastOpens).build();
        bookmark.setTextCommonData(textCommonData);
        return bookmark;
    }



// =============================================================================================== Util
//    private static boolean returnBool(int seed) {
//        return seed == 0;
//    }

    private static boolean parseBookmark(String seed) {
        boolean result;
        try {
            result = Integer.parseInt(seed) == 0;
        } catch (NumberFormatException e) {
            return false;
        }
        return result;
    }
}
