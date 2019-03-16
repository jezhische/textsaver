package com.jezh.textsaver.dto;

import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;

/** the DTO for transfer the proper data of TextPart instance plus links transfer, that supported by
 * ResourceSupport, the base class for DTOs to collect links */
@Data
//@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextPartResource extends ResourceSupport {

    private String body;
    private String name;
//    private long textPartId;

//    private boolean isEdited;

//    private boolean isBlank;

    private Date lastUpdate;
    private int pageNumber;
    private int totalPages;
    private List<BookmarkResource> bookmarkResources;

    @Override
    public String toString() {
        return "TextPartResource{" +
                "body='" + body + '\'' +
                ", username='" + name + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", currentPageNumber=" + pageNumber +
                ", totalPages=" + totalPages +
                ", bookmarkResources=" + bookmarkResources +
                ", links=" + getLinks() +
                '}';
    }
}
