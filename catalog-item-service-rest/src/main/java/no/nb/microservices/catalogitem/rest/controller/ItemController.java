package no.nb.microservices.catalogitem.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import no.nb.htrace.annotation.Traceable;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.ItemService;
import no.nb.microservices.catalogitem.rest.controller.assembler.ItemResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.controller.assembler.RelatedItemsResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.RelatedItemResource;

@RestController
@RequestMapping(value = "/catalog/items")
@Api(value = "/catalog/items", description = "Home api")
public class ItemController {
    private ItemService itemService;
    
    @Autowired
    public ItemController(ItemService itemService) {
        super();
        this.itemService = itemService;
    }

    @ApiOperation(value = "Hello World", notes = "Hello World notes", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful response") })
    @Traceable(description="item")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ItemResource> getItem(@PathVariable(value = "id") String id,
            @RequestParam(required=false) String expand) {
        Item item = itemService.getItemById(id, expand);
        
        ItemResource resource = new ItemResultResourceAssembler().toResource(item);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @Traceable(description="relatedItems")
    @RequestMapping(value = "/{id}/relatedItems", method = RequestMethod.GET)
    public ResponseEntity<RelatedItemResource> getRelatedItems(@PathVariable(value = "id") String id) {
        Item item = itemService.getItemById(id, "relatedItems");
        
        RelatedItemResource resource = new RelatedItemsResourceAssembler().toResource(item);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
