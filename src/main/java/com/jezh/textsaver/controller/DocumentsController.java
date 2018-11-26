package com.jezh.textsaver.controller;

import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.stereotype.Controller;

@Controller
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
public class DocumentsController {


}
