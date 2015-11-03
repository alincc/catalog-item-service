package no.nb.microservices.catalogitem.rest.controller.assembler;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * copy of https://github.com/spring-projects/spring-hateoas/blob/master/src/main/java/org/springframework/hateoas/mvc/ForwardedHeader.java
 */
class ForwardedHeader {

    public static final String NAME = "Forwarded";
    private static final ForwardedHeader NO_HEADER = new ForwardedHeader(Collections.<String, String> emptyMap());

    private final Map<String, String> elements;

    private ForwardedHeader(Map<String, String> elements) {
        this.elements = elements;
    }

    public static ForwardedHeader of(String source) {

        if (!StringUtils.hasText(source)) {
            return NO_HEADER;
        }

        Map<String, String> elements = new HashMap<String, String>();

        for (String part : source.split(";")) {

            String[] keyValue = part.split("=");

            if (keyValue.length != 2) {
                continue;
            }

            elements.put(keyValue[0].trim(), keyValue[1].trim());
        }

        Assert.notNull(elements, "Forwarded elements must not be null!");
        Assert.isTrue(!elements.isEmpty(), "At least one forwarded element needs to be present!");

        return new ForwardedHeader(elements);
    }

    public String getProto() {
        return elements.get("proto");
    }

    public String getHost() {
        return elements.get("host");
    }
}