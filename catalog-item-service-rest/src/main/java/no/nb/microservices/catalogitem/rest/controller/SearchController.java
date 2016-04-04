package no.nb.microservices.catalogitem.rest.controller;

import no.nb.htrace.annotation.Traceable;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchRequest;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.controller.assembler.SuperSearchResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.SuperItemSearchResource;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
        binder.registerCustomEditor(String[].class, "mediaTypes", new StringArrayPropertyEditor(","));
    }

    @Traceable(description="search")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<SuperItemSearchResource> superSearch(SuperSearchRequest searchRequest,
                                                               @PageableDefault Pageable pageable) {
        SuperSearchAggregated superSearchAggregated = searchService.superSearch(searchRequest, pageable);
        SuperItemSearchResource resource = new SuperSearchResultResourceAssembler().toResource(superSearchAggregated);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @Traceable(description="scroll")
    @RequestMapping(value = "/scroll", method = RequestMethod.GET)
    public ResponseEntity<ItemSearchResource> search(
            @RequestParam(value = "scrollId") String scrollId) {
        throw new NotImplementedException("Scroll not implemented");
    }
}
