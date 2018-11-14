package com.jezh.textsaver.businessLayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Manager {

    @Autowired
    ObjectMapper objectMapper;
}
