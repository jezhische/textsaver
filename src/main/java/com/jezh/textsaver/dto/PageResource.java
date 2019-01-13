package com.jezh.textsaver.dto;

import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResource extends ResourceSupport {
    private String body;
    private int pageNumber;
    private int totalPages;
//    private List<BookmarkResource> bookmarkResources;
}
