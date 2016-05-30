package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.rest.controller.SearchController;
import no.nb.microservices.catalogitem.rest.controller.SearchResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.SuperEmbeddedWrapper;
import no.nb.microservices.catalogitem.rest.model.SuperItemSearchResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public class SuperSearchResultResourceAssembler extends ResourceAssemblerSupport<SuperSearchAggregated, SuperItemSearchResource> {

    public SuperSearchResultResourceAssembler() {
        super(SearchController.class, SuperItemSearchResource.class);
    }

    @Override
    public SuperItemSearchResource toResource(SuperSearchAggregated superSearchAggregated) {
        SuperEmbeddedWrapper embeddedWrapper = getSuperEmbeddedWrapper(superSearchAggregated);
        SuperItemSearchResource resource = new SuperItemSearchResource(superSearchAggregated.getMetadata(), embeddedWrapper);
        resource.add(getSelfLink());
        return resource;
    }

    private SuperEmbeddedWrapper getSuperEmbeddedWrapper(SuperSearchAggregated superSearchAggregated) {
        SuperEmbeddedWrapper embeddedWrapper = new SuperEmbeddedWrapper();
        Map<String, SearchAggregated> searchAggregateds = superSearchAggregated.getSearchAggregateds();
        Map<String, ItemSearchResource> searchResources = new HashMap();
        for (Map.Entry<String, SearchAggregated> entry : searchAggregateds.entrySet()) {
            ItemSearchResource itemSearchResource = new SearchResultResourceAssembler().toResource(entry.getValue());
            searchResources.put(entry.getKey(), itemSearchResource);
        }
        embeddedWrapper.setSearchResults(searchResources);
        return embeddedWrapper;
    }

    private Link getSelfLink() {
        UriTemplate base = new UriTemplate(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        return new Link(new UriTemplate(builder.build().toString()), Link.REL_SELF);
    }
}
