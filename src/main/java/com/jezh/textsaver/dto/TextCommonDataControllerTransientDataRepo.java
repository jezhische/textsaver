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

//    private boolean isRunning;
    private Map<String, Long> docIds;
}
