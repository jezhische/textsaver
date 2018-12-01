package com.jezh.textsaver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextCommonDataControllerTransientDataRepo {

    /** the map to get textCommonData id by textCommonDataLinkedRepresentation name */
    private Map<String, Long> docIds;
}
