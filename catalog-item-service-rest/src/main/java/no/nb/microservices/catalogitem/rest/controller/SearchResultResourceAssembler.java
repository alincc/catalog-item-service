package no.nb.microservices.catalogitem.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.rest.controller.assembler.ItemResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.hateoas.mvc.UriComponentsContributor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class SearchResultResourceAssembler implements ResourceAssembler<SearchAggregated, ItemSearchResource> {

    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver = new HateoasPageableHandlerMethodArgumentResolver();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ItemSearchResource toResource(SearchAggregated result) {

        ItemSearchResource resources = new ItemSearchResource(asPageMetadata(result.getPage()));

        for (Item item : result.getPage().getContent()) {
            ItemResource itemResource = new ItemResultResourceAssembler().toResource(item);
            resources.getEmbedded().getItems().add(itemResource);
        }

        resources.getEmbedded().setContentSearch(result.getContentSearches());

        JsonNode jsonNode = objectMapper.convertValue(result.getAggregations(), JsonNode.class);
        resources.getEmbedded().setAggregations(jsonNode);

        return addPaginationLinks(resources, result);
    }

    private ItemSearchResource addPaginationLinks(ItemSearchResource resources, SearchAggregated result) {
        if (result.getScrollId() != null) {
            resources.add(linkTo(methodOn(SearchController.class).search(result.getScrollId())).withRel(Link.REL_NEXT));
        } else {
            Page<?> page = result.getPage();

            SearchRequest searchRequest = result.getSearchRequest();

            ControllerLinkBuilderFactory controllerLinkBuilderFactory = new ControllerLinkBuilderFactory();
            List<UriComponentsContributor> uriComponentsContributors = new ArrayList<>();
            uriComponentsContributors.add(new SearchRequestUriComponentsContributor());
            controllerLinkBuilderFactory.setUriComponentsContributors(uriComponentsContributors);
            String uri = controllerLinkBuilderFactory
                    .linkTo(methodOn(ItemController.class).search(searchRequest, new PageRequest(page.getNumber(), page.getSize())))
                    .toString();

            UriTemplate base = new UriTemplate(uri);

            if (page.hasPrevious()) {
                resources.add(createLink(base, new PageRequest(0, page.getSize(), page.getSort()), Link.REL_FIRST));
            }

            if (page.hasPrevious()) {
                resources.add(createLink(base, page.previousPageable(), Link.REL_PREVIOUS));
            }

            resources.add(createLink(base, null, Link.REL_SELF));

            if (page.hasNext()) {
                resources.add(createLink(base, page.nextPageable(), Link.REL_NEXT));
            }

            if (page.hasNext()) {

                int lastIndex = page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1;

                resources.add(createLink(base, new PageRequest(lastIndex, page.getSize(), page.getSort()), Link.REL_LAST));
            }
        }

        return resources;
    }

    private Link createLink(UriTemplate base, Pageable pageable, String rel) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        pageableResolver.enhance(builder, null, pageable);

        return new Link(new UriTemplate(builder.build().toString()), rel);
    }

    private static <T> PageMetadata asPageMetadata(Page<T> page) {

        return new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
    }
}

