package no.nb.microservices.catalogitem.core.search.model;

import no.nb.htrace.core.Traceable;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import org.apache.htrace.Span;
import org.apache.htrace.Trace;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ItemWrapper implements Traceable {
    private String id;
    private CountDownLatch latch;
    private List<Item> items;
    private Span span = Trace.currentSpan();
    private SearchRequest searchRequest;

    private SecurityInfo securityInfo = new SecurityInfo();

    public ItemWrapper(String id, CountDownLatch latch, List<Item> items, SearchRequest searchRequest) {
        this.id = id;
        this.latch = latch;
        this.items = items;
        this.searchRequest = searchRequest;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

    public SecurityInfo getSecurityInfo() {
        return securityInfo;
    }

    @Override
    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    public SearchRequest getSearchRequest() {
        return searchRequest;
    }

}
