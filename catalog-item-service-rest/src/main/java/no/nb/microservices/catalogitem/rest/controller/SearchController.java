package no.nb.microservices.catalogitem.rest.controller;

import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import no.nb.htrace.annotation.Traceable;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchRequest;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.SuperItemSearchResource;

@RestController
@RequestMapping(value = "/catalog/v1")
public class SearchController {

    private final ISearchService searchService;

    @Autowired
    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    @InitBinder
    public void sortBinderInit(WebDataBinder binder) {
        binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(null));
        binder.registerCustomEditor(String[].class, "mediatypes", new StringArrayPropertyEditor(","));
    }

    @Traceable(description="items")
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public ResponseEntity<ItemSearchResource> search(SearchRequest searchRequest, @PageableDefault Pageable pageable) {
        SearchAggregated result = searchService.search(searchRequest, pageable);
        ItemSearchResource resource = new SearchResultResourceAssembler().toResource(result);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @Traceable(description="search")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<SuperItemSearchResource> superSearch(SuperSearchRequest searchRequest,
                                                               @PageableDefault Pageable pageable) {
        SuperItemSearchResource resource = new SuperItemSearchResource();

        SuperSearchAggregated superSearchAggregated = searchService.superSearch(searchRequest, pageable);
        Map<String, SearchAggregated> searchAggregateds = superSearchAggregated.getSearchAggregateds();
        for(Map.Entry<String, SearchAggregated> entry : searchAggregateds.entrySet()) {
            ItemSearchResource itemSearchResource = new SearchResultResourceAssembler().toResource(entry.getValue());

            if ("bøker".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setBooks(itemSearchResource);
            } else if ("bilder".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setImages(itemSearchResource);
            } else if("artikler".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setJournals(itemSearchResource);
            } else if ("kart".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setMaps(itemSearchResource);
            } else if("film".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setMovies(itemSearchResource);
            } else if ("noter".equalsIgnoreCase(entry.getKey())){
                resource.getEmbedded().setMusicBooks(itemSearchResource);
            } else if ("musikk".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setMusic(itemSearchResource);
            } else if ("musikkmanuskripter".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setMusicManuscripts(itemSearchResource);
            } else if("aviser".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setNewspapers(itemSearchResource);
            } else if ("other".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setOthers(itemSearchResource);
            } else if ("plakater".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setPosters(itemSearchResource);
            } else if ("programrapporter".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setProgramReports(itemSearchResource);
            } else if ("radio".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setRadio(itemSearchResource);
            } else if ("lydopptak".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setSoundRecords(itemSearchResource);
            } else if ("fjernsyn".equalsIgnoreCase(entry.getKey())) {
                resource.getEmbedded().setTelevision(itemSearchResource);
            }
        }

        UriTemplate base = new UriTemplate(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        Link link = new Link(new UriTemplate(builder.build().toString()), Link.REL_SELF);
        resource.add(link);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @Traceable(description="scroll")
    @RequestMapping(value = "/scroll", method = RequestMethod.GET)
    public ResponseEntity<ItemSearchResource> search(
            @RequestParam(value = "scrollId") String scrollId) {
        throw new NotImplementedException("Scroll not implemented");
    }
}
