package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.controller.TextCommonDataController;
import com.jezh.textsaver.controller.TextPartController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DataManager {

//    private TextCommonDataControllerTransientDataRepo repository;
//
//    private TextCommonDataResourceAssembler assembler;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT;

    private Environment env;

    static {SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");}

    @Autowired
    public DataManager(Environment env) {
        this.env = env;
    }

    // TODO: to remove?
    public static String getUniqueName(String docDataName, Date createdDate) {
        if (createdDate != null)
            return new StringBuilder()
                    .append(docDataName)
                    .append("-created-date-")
                    .append(SIMPLE_DATE_FORMAT.format(createdDate)).toString();
        else
            return docDataName;
    }

    public static String getLastOpenedArrayItem(int pageNumber, boolean isEdited) {
        return String.valueOf(pageNumber) + (isEdited ? "1" : "0");
    }

    /**
     *
     * */
    public String createPageLink(long docDataId, int pageNumber) throws NoHandlerFoundException, UnknownHostException {
        String port = env.getRequiredProperty("local.server.port"); // by unknown reason property "server.port" in
        // spring boot 2 returns "-1", so this is a crutch
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        URI resourcePath = linkTo(methodOn(TextPartController.class)
                .findTextPartById(docDataId, pageNumber)).toUri();
        String uri = UriComponentsBuilder.newInstance().scheme("http")
                .host(hostAddress).port(port)
//                .path(env.getRequiredProperty("server.servlet.context-path")) // by unknown reason, from test the
//                context-path isn't created, and this line is necessary for test, but from controller this line
//                causes duplication of context-path, so I need to remove it for application run
                .path(resourcePath.getPath())
                .query(resourcePath.getQuery())
                .toUriString();
          return uri; // http://localhost:port/textsaver/doc-data/docDataId/pages?page=pageNumber
    }

    public static String trimQuotes(String obtainedName) {
        return obtainedName.substring(1, obtainedName.length() - 1);
    }

    // FIXME: 18.12.2018 IllegalArgumentException: Cannot subclass final class java.lang.String
    public static String createDocDataLink(String name) throws NoHandlerFoundException, UnknownHostException {
        return linkTo(methodOn(TextCommonDataController.class)
                .create(name))
                .withSelfRel().getHref();
    }


}
