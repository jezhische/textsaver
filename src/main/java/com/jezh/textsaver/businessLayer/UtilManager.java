package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.dto.TextCommonDataControllerTransientDataRepo;
import com.jezh.textsaver.dto.TextCommonDataLinkedRepresentation;
import com.jezh.textsaver.entity.TextCommonData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UtilManager {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT;

    static {SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");}

    /** set value of fields of textCommonData controller transient data repository */
    public static void setTextCommonDataControllerTransientDataRepo
    (List<TextCommonData> docsData,
     List<TextCommonDataLinkedRepresentation> linkedDocsData,
     TextCommonDataControllerTransientDataRepo repository) {
        for (int i = 0; i < linkedDocsData.size(); i++) {
            TextCommonDataLinkedRepresentation linkedDocData = linkedDocsData.get(i);
            TextCommonData docData = docsData.get(i);
            repository.getDocIds().put(linkedDocData.getName(), docData.getId());
        }
    }

    public static String getLinkedDocDataName(String docDataName, Date createdDate) {
        if (createdDate != null)
            return new StringBuilder()
                    .append(docDataName)
                    .append("-created-date-")
                    .append(SIMPLE_DATE_FORMAT.format(createdDate)).toString();
        else
            return docDataName;
    }
}
