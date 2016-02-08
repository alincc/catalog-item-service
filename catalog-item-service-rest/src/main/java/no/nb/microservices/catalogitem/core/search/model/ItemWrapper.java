package no.nb.microservices.catalogitem.core.search.model;

import no.nb.htrace.core.Traceable;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogsearchindex.ItemResource;
import org.apache.htrace.Span;
import org.apache.htrace.Trace;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ItemWrapper implements Traceable {
    private String id;
    private ItemResource itemResource;
    private CountDownLatch latch;
    private List<Item> items;
    private Span span = Trace.currentSpan();
    private SearchRequest searchRequest;

    private SecurityInfo securityInfo = new SecurityInfo();

    public ItemWrapper(ItemResource itemResource, CountDownLatch latch, List<Item> items, SearchRequest searchRequest) {
        this.itemResource = itemResource;
        this.latch = latch;
        this.items = items;
        this.searchRequest = searchRequest;
    }

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

    public ItemResource getItemResource() {
        return itemResource;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
