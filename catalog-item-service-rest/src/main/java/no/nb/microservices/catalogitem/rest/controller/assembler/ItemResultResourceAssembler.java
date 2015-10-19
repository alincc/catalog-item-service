package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.controller.ItemController;
import no.nb.microservices.catalogitem.rest.model.ItemResource;

public class ItemResultResourceAssembler implements ResourceAssembler<Item, ItemResource> {

    @Override
    public ItemResource toResource(Item item) {
        ItemResource resource = new ItemResource(item.getId());
        createLinks(item, resource);
        resource.setAccessInfo(new AccessInfoBuilder().fields(item.getField()).access(item.hasAccess()).build());
        resource.setMetadata(new MetadataBuilder(item).build());
        
        return resource;
    }

    private void createLinks(Item item, ItemResource resource) {
        resource.add(createSelfLink(item));
        resource.add(createModsLink(item));
        resource.add(createPresentationLink(item));
        resource.add(createEnwLink(item));
        resource.add(createRisLink(item));
        resource.add(createWikiLink(item));
    }

    private Link createSelfLink(Item item) {
        return linkTo(ItemController.class).slash(item).withSelfRel();
    }
    
    private Link createModsLink(Item item) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.MODS, item.getId()).withRel("mods");
    }
    
    private Link createPresentationLink(Item item) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.PRESENTATION, item.getId()).withRel("presentation");
    }

    private Link createEnwLink(Item item) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.ENW, item.getId()).withRel("enw");
    }
    
    private Link createRisLink(Item item) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.RIS, item.getId()).withRel("ris");
    }

    private Link createWikiLink(Item item) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.WIKI, item.getId()).withRel("wiki");
    }
}
