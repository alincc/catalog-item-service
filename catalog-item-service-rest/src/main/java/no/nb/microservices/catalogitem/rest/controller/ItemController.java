package no.nb.microservices.catalogitem.rest.controller;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.IItemService;
import no.nb.microservices.catalogitem.rest.model.ItemResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "/", description = "Home api")
public class ItemController {

    IItemService itemService;
    
    @Autowired
    public ItemController(IItemService itemService) {
        super();
        this.itemService = itemService;
    }

    @ApiOperation(value = "Hello World", notes = "Hello World notes", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful response") })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ItemResource> getItem(@PathVariable(value = "id") String id) {
        Item item = itemService.getItemById(id);
        
        ItemResource resource = new ItemResultResourceAssembler().toResource(item);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
    
    
}
