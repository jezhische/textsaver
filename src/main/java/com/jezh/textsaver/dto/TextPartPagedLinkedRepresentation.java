package com.jezh.textsaver.dto;

import com.jezh.textsaver.entity.TextCommonData;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/** the DTO for transfer the proper data of TextPart instance plus links transfer, that supported by
 * ResourceSupport, the base class for DTOs to collect links */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextPartPagedLinkedRepresentation extends ResourceSupport {

    private String body;
//    private Long nextItem;
    private Date lastUpdate;
    private int pageNumber;
    private int totalPages;
}
