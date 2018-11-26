package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.dto.TextPartControllerTransientDataRepo;
import com.jezh.textsaver.dto.TextPartPagedLinkedRepresentation;
import com.jezh.textsaver.entity.TextPart;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.NoHandlerFoundException;

@Service
public class TextPartPageRepresentationConverter {

    private MapperFactory mapperFactory;

    @Autowired
    public TextPartPageRepresentationConverter(MapperFactory mapperFactory) {
        mapperFactory.classMap(TextPart.class, TextPartPagedLinkedRepresentation.class)
                .field("body", "body")
//                .field("nextItem", "nextItem")
                .field("lastUpdate", "lastUpdate")
                .exclude("id")
                .exclude("textCommonData")
                .exclude("nextItem")
                .exclude("pageNumber")
                .exclude("totalPages")
                .exclude("bookmarksLinks")
                .register();
        this.mapperFactory = mapperFactory;
    }

    /** convert source TextPart instance to destination TextPartPagedLinkedRepresentation instance, using BoundMapperFacade<TextPart, TextPartPagedLinkedRepresentation> */
    public TextPartPagedLinkedRepresentation convertToLinkedPage(TextPart textPart) {
        return mapperFactory.getMapperFacade(TextPart.class, TextPartPagedLinkedRepresentation.class).map(textPart);
    }

    /** reverse conversion TextPartPagedLinkedRepresentation instance to TextPart instance, using BoundMapperFacade<TextPart, TextPartPagedLinkedRepresentation> */
    public TextPart convertToEntity(TextPartPagedLinkedRepresentation textPartPagedLinkedRepresentation) {
        return mapperFactory.getMapperFacade(TextPart.class, TextPartPagedLinkedRepresentation.class).mapReverse(textPartPagedLinkedRepresentation);
    }


}
