package no.nb.microservices.catalogitem.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import no.nb.microservices.catalogitem.core.item.service.Item;
import no.nb.microservices.catalogitem.rest.model.AccessInfo;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;

import org.springframework.hateoas.ResourceAssembler;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class ItemResultResourceAssembler implements ResourceAssembler<Item, ItemResource> {

    @Override
    public ItemResource toResource(Item item) {
        ItemResource resource = new ItemResource(item.getId());
        
        populateMetadata(item, resource);
        
        populateAccessInfo(item, resource);
        
        populateLinks(item, resource);
        
        return resource;
    }

    private void populateLinks(Item item, ItemResource resource) {
        resource.add(linkTo(ItemController.class).slash(item).withSelfRel());
    }

    private void populateMetadata(Item item, ItemResource resource) {
        Metadata metadata = new Metadata();
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle(item.getTitle());
        metadata.setTitleInfo(titleInfo);
        resource.setMetadata(metadata);
    }

    private void populateAccessInfo(Item item, ItemResource resource) {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setDigital(item.getAccessInfo().isDigital());
        resource.setAccessInfo(accessInfo);
    }

}
