package com.jezh.textsaver.dto;

import lombok.*;
import org.springframework.hateoas.ResourceSupport;

/** the DTO for transfer the proper data of Bookmark instance, provided with links transfer, that supported by
 * ResourceSupport, the base class for DTOs to collect links */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkLinkedRepresentation extends ResourceSupport {

    private String color;

}
