package com.jezh.textsaver.dto;

import lombok.*;
import org.springframework.hateoas.ResourceSupport;

/** the DTO for transfer the proper data of Bookmarks instance, provided with links transfer, that supported by
 * ResourceSupport, the base class for DTOs to collect links */
@Data
//@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResource /*extends ResourceSupport*/ {

    private int pageNumber;

    private String color;

    private String link;

}
