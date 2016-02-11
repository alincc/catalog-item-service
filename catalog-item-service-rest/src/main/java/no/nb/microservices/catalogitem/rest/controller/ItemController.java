package no.nb.microservices.catalogitem.rest.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/catalog/v1/items")
@Api(value = "/catalog/v1/items", description = "Home api")
public class ItemController {

    private final ItemService itemService;

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
            @RequestParam(required=false) List<String> fields,
            @RequestParam(required=false, defaultValue = "metadata") String expand) {
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
}
