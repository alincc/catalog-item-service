package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;

import no.nb.microservices.catalogitem.core.item.model.Item;

public class ThumbnailBuilder {
    private final Item item;

    public ThumbnailBuilder(Item item) {
        this.item = item;
    }

    public List<Link> build() {
        List<Link> links = new ArrayList<>();

        if  (item.getMods() != null && item.getMods().getLocation() != null && item.getMods().getLocation().getUrls() != null && !item.getMods().getLocation().getUrls().isEmpty()) {
            String thumbnailUrl = (item.getMods().getLocation().getUrls().stream()
                    .filter(q -> q.getAccess() != null && q.getAccess().equalsIgnoreCase("preview")).findAny()
                    .orElseGet(() -> item.getMods().getLocation().getUrls().get(0))).getValue();

            if (thumbnailUrl.contains("gallerinor")) {
                links.add(new Link(thumbnailUrl.replaceAll("size=\\d{1,2}", "size=16")).withRel("thumbnail_small"));
                links.add(new Link(thumbnailUrl.replaceAll("size=\\d{1,2}", "size=4")).withRel("thumbnail_large"));
            }
            else {
                links.add(new Link(thumbnailUrl).withRel("thumbnail_large"));
            }
        }
        else {
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getId(), 256).withRel("thumbnail_large"));
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getId(), 128).withRel("thumbnail_medium"));
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getId(), 64).withRel("thumbnail_small"));
        }

        return links;
    }
}