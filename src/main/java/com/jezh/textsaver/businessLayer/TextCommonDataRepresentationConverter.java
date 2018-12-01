package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.dto.TextCommonDataLinkedRepresentation;
import com.jezh.textsaver.entity.TextCommonData;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TextCommonDataRepresentationConverter {

    public TextCommonDataLinkedRepresentation convertToLinkedRepresentation(TextCommonData textCommonData) {
        Date createdDate = textCommonData.getCreatedDate();
        return TextCommonDataLinkedRepresentation.builder()
                .name(DocDataManager.getLinkedDocDataName(textCommonData.getName(), createdDate))
                .createdDate(createdDate)
                .updatedDate(textCommonData.getUpdatedDate())
                .build();
    }

    public TextCommonData convertToEntity(TextCommonDataLinkedRepresentation representation) {
        return null;
    }
}
