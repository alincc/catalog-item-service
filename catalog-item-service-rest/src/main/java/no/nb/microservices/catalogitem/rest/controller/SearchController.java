package no.nb.microservices.catalogitem.rest.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import no.nb.htrace.annotation.Traceable;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/catalog/items/search")
@Api(value = "/", description = "Home api")
public class SearchController {

    private final ISearchService searchService;

    @Autowired
    public SearchController(ISearchService searchService) {
        super();
        this.searchService = searchService;
    }

    @InitBinder
    public void sortBinderInit(WebDataBinder binder) {
        binder.registerCustomEditor(String[].class, "sort", new StringArrayPropertyEditor(null));
    }

    @ApiOperation(value = "Hello World", notes = "Hello World notes", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful response") })
    @Traceable(description="search")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemSearchResource> search(SearchRequest searchRequest, @PageableDefault Pageable pageable) {

        SearchAggregated result = searchService.search(searchRequest, pageable);

        ItemSearchResource resource = new SearchResultResourceAssembler().toResource(result);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
