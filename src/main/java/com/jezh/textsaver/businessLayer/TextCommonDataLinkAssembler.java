package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.controller.TextCommonDataController;
import com.jezh.textsaver.dto.TextCommonDataLinkedRepresentation;
import com.jezh.textsaver.entity.TextCommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class TextCommonDataLinkAssembler {

    private TextCommonDataRepresentationConverter converter;

    @Autowired
    public TextCommonDataLinkAssembler(TextCommonDataRepresentationConverter converter) {
        this.converter = converter;
    }

    public List<TextCommonDataLinkedRepresentation> getLinkedDocsData(List<TextCommonData> docsData) {
        return docsData.stream().map(docData -> {
            TextCommonDataLinkedRepresentation linkedDocData = converter.convertToLinkedRepresentation(docData);
            linkedDocData.add(
//                    linkTo(
//                    methodOn(TextCommonDataController.class).findByLinkedDocName(linkedDocData.getName()))
//                    .withSelfRel()); // don't work - why?
//            linkTo(TextCommonDataController.class).slash("documents").slash(linkedDocData.getName()).withSelfRel()); // working
                    linkTo(methodOn(TextCommonDataController.class).getLinkedTextCommonDataList())
                            .slash(linkedDocData.getName())
                            .withSelfRel());
            return linkedDocData;
        }).collect(Collectors.toList());
    }
}
