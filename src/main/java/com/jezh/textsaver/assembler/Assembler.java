package com.jezh.textsaver.assembler;

import com.jezh.textsaver.service.TextPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class Assembler {

    private final TextPartService textPartService;

    @Autowired
    public Assembler(TextPartService textPartService) {
        this.textPartService = textPartService;
    }

//    public String deserialize
}
