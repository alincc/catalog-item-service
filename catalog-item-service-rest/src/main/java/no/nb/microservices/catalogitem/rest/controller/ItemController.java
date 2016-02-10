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
import no.nb.microservices.catalogitem.core.search.model.SearchRequestBuilder;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.controller.assembler.ItemResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.controller.assembler.RelatedItemsResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.RelatedItemResource;
import no.nb.microservices.catalogitem.rest.model.SuperItemSearchResource;
import no.nb.microservices.catalogsearchindex.NBSearchType;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/catalog/v1/items")
@Api(value = "/catalog/v1/items", description = "Home api")
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
        binder.registerCustomEditor(
                String[].class,
                new StringArrayPropertyEditor(null));
    }

    @ApiOperation(value = "Hello World", notes = "Hello World notes", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful response") })
    @Traceable(description="item")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ItemResource> getItem(@PathVariable(value = "id") String id,
            @RequestParam(required=false) List<String> fields,
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
    public ResponseEntity<ItemSearchResource> search(@RequestParam("q") String q,
                                                     @RequestParam(value = "aggs", required = false) String aggs,
                                                     @RequestParam(value = "searchType", required = false) NBSearchType searchType,
                                                     @RequestParam(value = "filter", required = false) String[] filter,
                                                     @RequestParam(value = "boost", required = false) String[] boost,
                                                     @RequestParam(value = "bottomLeft", required = false) String bottomLeft,
                                                     @RequestParam(value = "topRight", required = false) String topRight,
                                                     @RequestParam(value = "precision", required = false) String precision,
                                                     @RequestParam(value = "fields", required = false) String[] fields,
                                                     @RequestParam(value = "explain", required = false) boolean explain,
                                                     @RequestParam(value = "grouping", required = false) boolean grouping,
                                                     @RequestParam(value = "should", required = false) String[] should,
                                                     @RequestParam(value = "sort", required = false) String[] sort,
                                                     @PageableDefault Pageable pageable) {

        SearchRequest searchRequest = new SearchRequestBuilder(q, aggs, searchType, filter, boost, bottomLeft, topRight,
                precision, fields, sort, explain, grouping, should).build();

        SearchAggregated result = searchService.search(searchRequest, pageable);

        ItemSearchResource resource = new SearchResultResourceAssembler().toResource(result);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @ApiOperation(value = "SuperSearch", notes = "SuperSearch", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful response") })
    @Traceable(description="superSearch")
    @RequestMapping(value = "/superSearch", method = RequestMethod.GET)
    public ResponseEntity<SuperItemSearchResource> superSearch(SearchRequest searchRequest,
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
    @RequestMapping(value = "/search/scroll", method = RequestMethod.GET)
    public ResponseEntity<ItemSearchResource> search(
            @RequestParam(value = "scrollId") String scrollId) {
        throw new NotImplementedException("Scroll not implemented");
    }    

}
