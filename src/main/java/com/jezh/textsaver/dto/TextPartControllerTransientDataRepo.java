package com.jezh.textsaver.dto;

import com.jezh.textsaver.entity.TextPart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * The repository to save transient data that TextPartController methods need
 * */

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextPartControllerTransientDataRepo {
    /** when {@code TextPartController} method {@code findPageByTextCommonDataId(long, int, HttpServletRequest)} is called
     * first time, it fill this list with found textPart ids, sorted in proper order, so that the number of the element of the
     * list matches the requested page number, and the size of the list matches the total page number.
     * @see com.jezh.textsaver.controller.TextPartController */
    private List<Long> listOfSortedTextPartId;
    private boolean isRunning;
    private int pageNumber;
    private int totalPages;

//    private static TextPartControllerTransientDataRepo instance;
//
//    private TextPartControllerTransientDataRepo(List<Long> listOfSortedTextPartId, boolean isRunning, int pageNumber, int totalPages) {
//        this.listOfSortedTextPartId = listOfSortedTextPartId;
//        this.isRunning = isRunning;
//        this.pageNumber = pageNumber;
//        this.totalPages = totalPages;
//    }
//
//    public static TextPartControllerTransientDataRepo getInstance(
//            List<Long> listOfSortedTextPartId, boolean isRunning, int pageNumber, int totalPages) { // #3
//        if(instance == null){
//            instance = new TextPartControllerTransientDataRepo(
//                    listOfSortedTextPartId, isRunning, pageNumber, totalPages);
//        }
//        return instance;
//    }
}
