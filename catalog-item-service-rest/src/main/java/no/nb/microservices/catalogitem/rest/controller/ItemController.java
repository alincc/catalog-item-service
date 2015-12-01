package no.nb.microservices.catalogitem.rest.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import no.nb.htrace.annotation.Traceable;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.ItemService;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.controller.assembler.ItemResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.controller.assembler.RelatedItemsResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.RelatedItemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/catalog/items")
@Api(value = "/v1/catalog/items", description = "Home api")
public class ItemController {

    private final ISearchService searchService;
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService, ISearchService searchService) {
        super();
        this.itemService = itemService;
        this.searchService = searchService;
    }

    @InitBinder
    public void sortBinderInit(WebDataBinder binder) {
        binder.registerCustomEditor(String[].class, "sort", new StringArrayPropertyEditor(null));
    }

    @ApiOperation(value = "Hello World", notes = "Hello World notes", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful response") })
    @Traceable(description="item")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ItemResource> getItem(@PathVariable(value = "id") String id,
            @RequestParam(required=false) String fields,
            @RequestParam(required=false) String expand) {
        Item item = itemService.getItemById(id, fields, expand);
        ItemResource resource = new ItemResultResourceAssembler().toResource(item);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @Traceable(description="relatedItems")
    @RequestMapping(value = "/{id}/relatedItems", method = RequestMethod.GET)
    public ResponseEntity<RelatedItemResource> getRelatedItems(@PathVariable(value = "id") String id) {
        Item item = itemService.getItemById(id, null, "relatedItems");
        
        RelatedItemResource resource = new RelatedItemsResourceAssembler().toResource(item);
        return new ResponseEntity<>(resource, HttpStatus.OK);
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
