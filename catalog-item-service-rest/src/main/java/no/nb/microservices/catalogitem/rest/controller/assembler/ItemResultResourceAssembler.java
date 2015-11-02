package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.controller.ItemController;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;

public class ItemResultResourceAssembler implements ResourceAssembler<Item, ItemResource> {

    @Override
    public ItemResource toResource(Item item) {
        ItemResource resource = new ItemResource(item.getId());
        createLinks(item, resource);
        resource.setAccessInfo(new AccessInfoBuilder().fields(item.getField()).access(item.hasAccess()).build());
        resource.setMetadata(new MetadataBuilder(item).withRelatedItems(item.getRelatedItems()).build());
        
        return resource;
    }

    private void createLinks(Item item, ItemResource resource) {
        StreamingInfoStrategy streamingInfoStrategy = StreamingInfoFactory.getStreamingInfoStrategy(getFirstMediatype(item));
        resource.add(createSelfLink(item));
        resource.add(createModsLink(item));
        resource.add(createPresentationLink(item));
        resource.add(createEnwLink(item));
        resource.add(createRisLink(item));
        resource.add(createWikiLink(item));
        
        if (streamingInfoStrategy.hasStreamingLink()) {
            resource.add(createPlaylistLink(item));
        }
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

    private Link createPlaylistLink(Item item) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.PLAYLIST, item.getId()).withRel("playlist");
    }

    private String getFirstMediatype(Item item) {
        List<String> mediaTypes = getMediaTypes(item.getField());
        if (mediaTypes != null && !mediaTypes.isEmpty()) {
            return mediaTypes.get(0);
        } else {
            return null;
        }
    }
    
    private List<String> getMediaTypes(FieldResource field) {
        if (field != null) {
            return field.getMediaTypes();
        } else {
            return Collections.emptyList();
        }
    }
}
