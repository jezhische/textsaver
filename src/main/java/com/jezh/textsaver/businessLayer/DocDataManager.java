package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.dto.TextCommonDataControllerTransientDataRepo;
import com.jezh.textsaver.dto.TextCommonDataLinkedRepresentation;
import com.jezh.textsaver.entity.TextCommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocDataManager {

    private TextCommonDataControllerTransientDataRepo repository;

    private TextCommonDataLinkAssembler assembler;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT;

    static {SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");}

    @Autowired
    public DocDataManager(TextCommonDataControllerTransientDataRepo repository,
                          TextCommonDataLinkAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /** set value of fields of textCommonData controller transient data repository */
    public List<TextCommonDataLinkedRepresentation> setTextCommonDataControllerTransientDataRepo
    (List<TextCommonData> docsData) {
        // create List<TextCommonDataLinkedRepresentation> linkedDocsData
        List<TextCommonDataLinkedRepresentation> linkedDocsData = assembler.getLinkedDocsData(docsData);
        // assign docIds in repository
        repository.setDocIds(new HashMap<String, Long>());
        // propagate docIds map in repository
        for (int i = 0; i < linkedDocsData.size(); i++) {
            TextCommonDataLinkedRepresentation linkedDocData = linkedDocsData.get(i);
            TextCommonData docData = docsData.get(i);
            repository.getDocIds().put(linkedDocData.getName(), docData.getId());
        }
        return linkedDocsData;
    }

    public Map<String, Long> getDocIds() {
        return repository.getDocIds();
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
