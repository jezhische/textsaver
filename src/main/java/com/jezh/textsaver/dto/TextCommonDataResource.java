package com.jezh.textsaver.dto;

import lombok.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//@Getter
//@Setter
//@ToString
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextCommonDataResource extends ResourceSupport {

    private String name;
    private Date createdDate;
    private Date updatedDate;

    @Override
    public String toString() {
        return "TextCommonDataResource{" +
                "name='" + name + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", _links=" + getLinks() +
                '}';
    }
}
