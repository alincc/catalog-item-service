package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.hateoas.core.LinkBuilderSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

class ResourceLinkBuilder extends LinkBuilderSupport<ResourceLinkBuilder> {
    
    ResourceLinkBuilder(UriComponentsBuilder builder) {
        super(builder);
    }

    public static ResourceLinkBuilder linkTo(ResourceTemplateLink resourceTemplate, Object... parameters) {
        UriTemplate template = new UriTemplate(resourceTemplate.getTemplate());
        URI uri = template.expand(parameters);
        return new ResourceLinkBuilder(getBuilder()).slash(uri);
    }

    /**
     * Copy from https://github.com/spring-projects/spring-hateoas/blob/master/src/main/java/org/springframework/hateoas/mvc/ControllerLinkBuilder.java
     */
    static UriComponentsBuilder getBuilder() {
        HttpServletRequest request = getCurrentRequest();
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromServletMapping(request);

        ForwardedHeader forwarded = ForwardedHeader.of(request.getHeader(ForwardedHeader.NAME));
        String proto = StringUtils.hasText(forwarded.getProto()) ? forwarded.getProto() : request.getHeader("X-Forwarded-Proto");
        String forwardedSsl = request.getHeader("X-Forwarded-Ssl");

        if (StringUtils.hasText(proto)) {
            builder.scheme(proto);
        } else if (StringUtils.hasText(forwardedSsl) && "on".equalsIgnoreCase(forwardedSsl)) {
            builder.scheme("https");
        }

        String host = forwarded.getHost();
        host = StringUtils.hasText(host) ? host : request.getHeader("X-Forwarded-Host");

        if (!StringUtils.hasText(host)) {
            return builder;
        }

        String[] hosts = StringUtils.commaDelimitedListToStringArray(host);
        String hostToUse = hosts[0];

        if (hostToUse.contains(":")) {

            String[] hostAndPort = StringUtils.split(hostToUse, ":");

            builder.host(hostAndPort[0]);
            builder.port(Integer.parseInt(hostAndPort[1]));

        } else {
            builder.host(hostToUse);
            builder.port(-1); // reset port if it was forwarded from default port
        }

        String port = request.getHeader("X-Forwarded-Port");

        if (StringUtils.hasText(port)) {
            builder.port(Integer.parseInt(port));
        }

        return builder;
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    @Override
    protected ResourceLinkBuilder getThis() {
        return this;
    }

    @Override
    protected ResourceLinkBuilder createNewInstance(
            UriComponentsBuilder builder) {
        return new ResourceLinkBuilder(builder);
    }
}