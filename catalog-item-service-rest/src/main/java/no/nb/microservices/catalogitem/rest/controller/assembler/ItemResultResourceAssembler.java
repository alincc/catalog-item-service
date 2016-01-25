package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.utils.ItemFields;
import no.nb.microservices.catalogitem.rest.controller.ItemController;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.RelatedItem;

public class ItemResultResourceAssembler extends ResourceAssemblerSupport<Item, ItemResource> {
    
    public ItemResultResourceAssembler() {
        super(ItemController.class, ItemResource.class);
    }

    @Override
    public ItemResource toResource(Item item) {
        ItemResource resource = new ItemResource(item.getId());
        if (ItemFields.show(item.getFields(), "_links")) {
            createLinks(item, resource);
        }

        if (ItemFields.show(item.getFields(), "title")) {
            resource.setTitle(createTitle(item));
        }
        
        if (hasRelatedItems(item)) {
            resource.setExpand("relatedItems");
        }
        
        if (ItemFields.show(item.getFields(), "accessInfo")) {
            resource.setAccessInfo(new AccessInfoBuilder()
                    .setItemResource(item.getItemResource())
                    .access(item.hasAccess())
                    .build());
        }
        
        if (ItemFields.show(item.getFields(), "metadata")) {
            resource.setMetadata(new MetadataBuilder()
                    .withItem(item)
                    .build());
        }

        resource.setRelatedItems(new RelatedItemsBuilder()
                .withRelatedItems(item.getRelatedItems())
                .build());

        return resource;
    }

    private String createTitle(Item item) {
        if (item.getItemResource() != null) {
            return item.getItemResource().getTitle();
        } else {
            return null;
        }
    }

    private void createLinks(Item item, ItemResource resource) {
        StreamingInfoStrategy streamingInfoStrategy = StreamingInfoFactory.getStreamingInfoStrategy(getFirstMediatype(item));
        resource.add(createSelfLink(item));
        resource.add(createModsLink(item));
        resource.add(createPresentationLink(item));
        resource.add(createEnwLink(item));
        resource.add(createRisLink(item));
        resource.add(createWikiLink(item));
        resource.add(createThumbnailLinks(item));
        if (hasRelatedItems(item)) {
            resource.add(createRelatedItemsLink(item));
        }

        if (streamingInfoStrategy.hasStreamingLink()) {
            resource.add(createPlaylistLink(item));
        }
    }

    private boolean hasRelatedItems(Item item) {
        long count = 0;
        if(item.getMods() != null && item.getMods().getRelatedItems() != null) {
           count = item.getMods().getRelatedItems().stream()
               .filter(r -> isSupportedRelatedItems(r) && hasRelatedItemIdentifier(r))
               .count();
           
        }
        return count > 0 ? true :false;
    }

    private boolean isSupportedRelatedItems(RelatedItem r) {
        return "constituent".equals(r.getType()) 
                   || "host".equals(r.getType()) 
                   || "preceding".equals(r.getType()) 
                   || "succeeding".equals(r.getType())
                   || "series".equals(r.getType());
    }

    private boolean hasRelatedItemIdentifier(RelatedItem r) {
        return (r.getRecordInfo() != null && r.getRecordInfo().getRecordIdentifier() != null)
                   || r.getIdentifier() != null;
    }

    private Link createSelfLink(Item item) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.ITEM_SELF, item.getId()).withSelfRel();
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

    private Link createRelatedItemsLink(Item item) {
        return ResourceLinkBuilder.linkTo(ResourceTemplateLink.RELATED_ITEMS, item.getId()).withRel("relatedItems");
    }

    private String getFirstMediatype(Item item) {
        if (item.getItemResource() != null && !item.getItemResource().getMediaTypes().isEmpty()) {
            return item.getItemResource().getMediaTypes().get(0);
        } else {
            return null;
        }
    }
    
    private List<Link> createThumbnailLinks(Item item) {
        return new ThumbnailBuilder()
                .withItemResource(item.getItemResource())
                .withMods(item.getMods())
                .build();
    }
}
