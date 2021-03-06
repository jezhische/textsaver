package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.controller.TextCommonDataController;
import com.jezh.textsaver.dto.TextCommonDataResource;
import com.jezh.textsaver.entity.TextCommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class TextCommonDataResourceAssembler {

    private DataManager dataManager;

    @Autowired
    public TextCommonDataResourceAssembler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<TextCommonDataResource> getLinkedDocsData(List<TextCommonData> docsData) {
        return docsData.stream().map(docData -> { // .map(Function<TextCommonData, TextCommonDataResource>)
            TextCommonDataResource linkedDocData = null;
            try {
                linkedDocData = convertToLinkedRepresentation(docData);
            } catch (UnknownHostException | NoHandlerFoundException e) {
                e.printStackTrace();
            }
            return linkedDocData;
        }).collect(Collectors.toList());
    }

    public TextCommonDataResource convertToLinkedRepresentation(TextCommonData docData) throws UnknownHostException, NoHandlerFoundException {
        TextCommonDataResource resource = TextCommonDataResource.builder()
                .name(docData.getName())/*(DataManager.getUniqueName(textCommonData.getName(), createdDate))*/ //todo: perhaps to remove
                .createdDate(docData.getCreatedDate())
                .updatedDate(docData.getUpdatedDate())
                .build();
                // link to page 0 uses as link to the whole document
                resource.add(new Link(dataManager.createPageLink(docData.getId(), 0))/*.withSelfRel()*/);
                return resource;
    }

    /*
     * for future needs
     */
//    public TextCommonData convertToEntity(TextCommonDataResource representation) {
//        return null;
//    }
}
