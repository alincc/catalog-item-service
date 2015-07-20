package no.nb.microservices.catalogitem.core.item.service;

import java.io.Serializable;

import org.apache.htrace.Span;

import no.nb.htrace.core.Traceable;

public class TracableId implements Traceable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final Span span;
    private final SecurityInfo securityInfo;
    
    public TracableId(Span span, String id, SecurityInfo securityInfo) {
        this.span = span;
        this.id = id;
        this.securityInfo = securityInfo;
    }
    
    @Override
    public Span getSpan() {
        return span;
    }

    public String getId() {
        return id;
    }

    public SecurityInfo getSecurityInfo() {
        return securityInfo;
    }

}
