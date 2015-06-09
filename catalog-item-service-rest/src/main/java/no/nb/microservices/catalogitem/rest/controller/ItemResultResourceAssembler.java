package no.nb.microservices.catalogitem.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import no.nb.microservices.catalogitem.core.item.service.Item;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;

import org.springframework.hateoas.ResourceAssembler;

public class ItemResultResourceAssembler implements ResourceAssembler<Item, ItemResource> {

    @Override
    public ItemResource toResource(Item item) {
        ItemResource resource = new ItemResource(item.getId());
        Metadata metadata = new Metadata();
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle(item.getTitle());
        metadata.setTitleInfo(titleInfo);
        resource.setMetadata(metadata);
        
        resource.add(linkTo(ItemController.class).slash(item).withSelfRel());
        
        return resource;
    }

}
