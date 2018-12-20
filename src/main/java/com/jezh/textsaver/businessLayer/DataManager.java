package com.jezh.textsaver.businessLayer;

import com.jezh.textsaver.controller.TextCommonDataController;
import com.jezh.textsaver.controller.TextPartController;
import de.escalon.hypermedia.spring.AffordanceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DataManager {

//    private TextCommonDataControllerTransientDataRepo repository;
//
//    private TextCommonDataLinkAssembler assembler;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT;

    private Environment env;

    static {SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");}

    @Autowired
    public DataManager(Environment env) {
        this.env = env;
    }

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
        String port = env.getRequiredProperty("local.server.port"); // with unknown reason property "server.port" in
        // spring boot 2 returns "-1", so this is a crutch
        String contextPath = env.getRequiredProperty("server.servlet.context-path");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String uri = UriComponentsBuilder.newInstance().scheme("http")
                .host(hostAddress).port(port).path(contextPath).path("/doc-data/{commonDataId}/pages")
                .query("page={pageNumber}").buildAndExpand(docDataId, "1").toString();
        // https://github.com/dschulten/hydra-java/blob/master/README.asciidoc#affordancebuilder-for-rich-hyperlinks
//        return AffordanceBuilder.linkTo(methodOn(TextPartController.class)
//                .findTextPartById(docDataId, pageNumber)).rel("self").build().toString();
//        return linkTo(methodOn(TextPartController.class)
//                .findTextPartById(docDataId, pageNumber))
//                .toUriComponentsBuilder().port(port).toUriString();
          return uri; // http://localhost:port/textsaver/doc-data/docDataId/pages?page=pageNumber
    }

    // FIXME: 18.12.2018 IllegalArgumentException: Cannot subclass final class java.lang.String
    public static String createDocDataLink(String name) throws NoHandlerFoundException, UnknownHostException {
        return linkTo(methodOn(TextCommonDataController.class)
                .create(name))
                .withSelfRel().getHref();
    }


}
