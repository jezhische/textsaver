package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.controller.TextCommonDataController;
import com.jezh.textsaver.controller.TextPartController;
import com.jezh.textsaver.dto.TextCommonDataControllerTransientDataRepo;
import com.jezh.textsaver.dto.TextCommonDataResource;
import com.jezh.textsaver.entity.TextCommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DataManager {

//    private TextCommonDataControllerTransientDataRepo repository;
//
//    private TextCommonDataLinkAssembler assembler;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT;

    static {SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");}


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
    public static String createPageLink(long docDataId, int pageNumber) throws NoHandlerFoundException {
        return linkTo(methodOn(TextPartController.class)
                .findPageByTextCommonDataIdAndPageNumber(docDataId, pageNumber, null))
                .withSelfRel().getHref();
    }

    public static String createDocDataLink(String name) throws NoHandlerFoundException {
        return linkTo(methodOn(TextCommonDataController.class)
                .create(name))
                .withSelfRel().getHref();
    }


}
