package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.controller.TextPartController;
import com.jezh.textsaver.dto.TextPartControllerTransientDataRepo;
import com.jezh.textsaver.dto.TextPartPagedLinkedRepresentation;
import com.jezh.textsaver.entity.TextPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TextPartLinkAssembler  {

    private TextPartPageRepresentationConverter converter;

    @Autowired
    public TextPartLinkAssembler(TextPartPageRepresentationConverter converter) {
        this.converter = converter;
    }

    public TextPartPagedLinkedRepresentation getLinkedPage(
            TextPart textPart, long commonDataId, int pageNumber, HttpServletRequest request,
            TextPartControllerTransientDataRepo repository) throws NoHandlerFoundException {
        TextPartPagedLinkedRepresentation page = converter.convertToLinkedPage(textPart);
//        TextPartPagedLinkedRepresentation page = convertToLinkedPage(textPart);
        page.setTotalPages(repository.getTotalPages());
        page.setPageNumber(pageNumber);
        if (pageNumber >= 1 && pageNumber <= repository.getTotalPages())
            page.add(linkTo(methodOn(TextPartController.class).findPageByTextCommonDataIdAndPageNumber(
                    commonDataId, 1, request)).withRel("first"));
        if (pageNumber > 2 && pageNumber <= repository.getTotalPages())
            page.add(linkTo(methodOn(TextPartController.class).findPageByTextCommonDataIdAndPageNumber(
                    commonDataId, pageNumber - 1, request)).withRel("previous"));
//        if (page_number > 1 && page_number < repository.getTotalPages())
            page.add(linkTo(methodOn(TextPartController.class).findPageByTextCommonDataIdAndPageNumber(
                    commonDataId, pageNumber, request)).withSelfRel());
        if (pageNumber >=1 && pageNumber < repository.getTotalPages() - 1)
            page.add(linkTo(methodOn(TextPartController.class).findPageByTextCommonDataIdAndPageNumber(
                    commonDataId, pageNumber + 1, request)).withRel("next"));
        if (pageNumber >=1 && pageNumber <= repository.getTotalPages())
            page.add(linkTo(methodOn(TextPartController.class).findPageByTextCommonDataIdAndPageNumber(
                    commonDataId, repository.getTotalPages(), request)).withRel("last"));
        return page;
    }

//    private TextPartPagedLinkedRepresentation convertToLinkedPage(TextPart textPart) {
//        return TextPartPagedLinkedRepresentation.builder()
//                .body(textPart.getBody())
//                .lastUpdate(textPart.getLastUpdate())
//                .build();
//    }
}
