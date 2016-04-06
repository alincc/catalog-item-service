package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.utils.ItemUtils;
import no.nb.microservices.catalogitem.rest.controller.ItemController;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.RelatedItem;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class ItemResultResourceAssembler extends ResourceAssemblerSupport<Item, ItemResource> {

    public static final List<String> SUPPORTED_RELATED_ITEMS = Arrays.asList("constituent", "host", "preceding", "succeeding", "series");
    public ItemResultResourceAssembler() {
        super(ItemController.class, ItemResource.class);
    }

    @Override
    public ItemResource toResource(Item item) {
        StringJoiner expand = new StringJoiner(",");
        expand.add("metadata");

        ItemResource resource = new ItemResource(item.getId());
        if (ItemUtils.showField(item.getFields(), "_links")) {
            createLinks(item, resource);
        }

        if (hasRelatedItems(item)) {
            expand.add("relatedItems");
        }

        resource.setExpand(expand.toString());

        if (ItemUtils.showField(item.getFields(), "accessInfo")) {
            resource.setAccessInfo(
                    new AccessInfoBuilder().setItemResource(item.getItemResource()).access(item.hasAccess()).build());
        }

        if (ItemUtils.isExpand(item.getExpand(), "metadata")) {
            resource.setMetadata(new MetadataBuilder().withItem(item).withExpand().build());
        } else {
            resource.setMetadata(new MetadataBuilder().withItem(item).build());
        }

        if (item.getItemResource() != null && item.getItemResource().getExplain() != null) {
            resource.setExplain(item.getItemResource().getExplain());
        }

        resource.setRelatedItems(new RelatedItemsBuilder().withRelatedItems(item.getRelatedItems()).build());

        return resource;
    }

    private void createLinks(Item item, ItemResource resource) {
        StreamingInfoStrategy streamingInfoStrategy = StreamingInfoFactory
                .getStreamingInfoStrategy(getFirstMediatype(item));
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
        if (item.getMods() != null && item.getMods().getRelatedItems() != null) {
            count = item.getMods().getRelatedItems().stream()
                    .filter(r -> isSupportedRelatedItems(r) && hasRelatedItemIdentifier(r)).count();

        }
        return count > 0;
    }

    private boolean isSupportedRelatedItems(RelatedItem r) {
        return ItemResultResourceAssembler.SUPPORTED_RELATED_ITEMS.contains(r.getType());
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
        return new ThumbnailBuilder().withItemResource(item.getItemResource()).withMods(item.getMods()).build();
    }
}
