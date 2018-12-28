package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.dto.BookmarkResource;
import com.jezh.textsaver.dto.PageResource;
import com.jezh.textsaver.dto.TextPartResource;
import com.jezh.textsaver.entity.Bookmarks;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.entity.entityProperties.EditedColorStore;
import com.jezh.textsaver.entity.entityProperties.OpenedColorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.UnknownHostException;
import java.util.*;

@Component
public class PageResourceAssembler {


    private DataManager dataManager;

    @Autowired
    public PageResourceAssembler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * supply TextPartResources with links to current, adjacent, first and last pages and last opened/edited pages
     * */
    public PageResource getResource(Page<TextPart> currentPage)
            throws IndexOutOfBoundsException, NoHandlerFoundException, UnknownHostException {
        TextPart textPart = currentPage.getContent().get(0);
        int pageNumber = currentPage.getNumber() + 1;
        PageResource resource = PageResource.builder()
                .body(textPart.getBody())
                .pageNumber(pageNumber)
                .totalPages(currentPage.getTotalPages())
                .build();
        resource.add(new Link(dataManager.createPageLink(textPart.getId(), pageNumber)));
        return resource;
    }
}
