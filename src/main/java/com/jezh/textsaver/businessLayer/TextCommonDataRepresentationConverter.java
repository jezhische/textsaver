package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.dto.TextCommonDataResource;
import com.jezh.textsaver.entity.TextCommonData;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TextCommonDataRepresentationConverter {

    public TextCommonDataResource convertToLinkedRepresentation(TextCommonData textCommonData) {
        Date createdDate = textCommonData.getCreatedDate();
        return TextCommonDataResource.builder()
                .name(DataManager.getUniqueName(textCommonData.getName(), createdDate))
                .createdDate(createdDate)
                .updatedDate(textCommonData.getUpdatedDate())
                .build();
    }

    public TextCommonData convertToEntity(TextCommonDataResource representation) {
        return null;
    }
}
