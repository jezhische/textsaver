package com.jezh.textsaver.dto;

import com.jezh.textsaver.entity.TextPart;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * The repository to save transient data that TextPartController methods need
 * */

//@Component
@Data
@Builder
public class TextPartControllerTransientDataRepo {
    private List<Long> sortedTextPartIdList;
    private long totalElements;
    private long totalPages;
    private PagedResources<TextPart> pagedResources;

}
