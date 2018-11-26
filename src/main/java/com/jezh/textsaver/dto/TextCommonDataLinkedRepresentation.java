package com.jezh.textsaver.dto;

import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Date;

//@Getter
//@Setter
//@ToString
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextCommonDataLinkedRepresentation extends ResourceSupport {

    private String name;
    private Date createdDate;
    private Date updatedDate;



}
