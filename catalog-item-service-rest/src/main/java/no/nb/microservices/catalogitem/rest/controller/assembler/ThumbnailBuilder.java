package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Url;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public class ThumbnailBuilder {
    private final Item item;

    public ThumbnailBuilder(Item item) {
        this.item = item;
    }

    public List<Link> build() {
        List<Link> links = new ArrayList<>();

        if (StringUtils.isNotEmpty(item.getField().getThumbnailUrl())) {
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getField().getThumbnailUrl(), 256).withRel("thumbnail_large"));
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getField().getThumbnailUrl(), 128).withRel("thumbnail_medium"));
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getField().getThumbnailUrl(), 64).withRel("thumbnail_small"));
        }
        else if (item.getMods() != null
                && item.getMods().getLocation() != null
                && item.getMods().getLocation().getUrls() != null
                && !item.getMods().getLocation().getUrls().isEmpty())
        {
            String thumbnailUrl = "";
            String location = (item.getMods().getLocation().getUrls().stream()
                    .filter(q -> q.getAccess() != null && q.getAccess().equalsIgnoreCase("preview")).findAny()
                    .orElseGet(() -> item.getMods().getLocation().getUrls().get(0))).getValue();
            thumbnailUrl = location.startsWith("http://") ? location : "";

            if (StringUtils.isNotEmpty(thumbnailUrl)) {
                if (thumbnailUrl.contains("gallerinor")) {
                    links.add(new Link(thumbnailUrl.replaceAll("size=\\d{1,2}", "size=16")).withRel("thumbnail_small"));
                    links.add(new Link(thumbnailUrl.replaceAll("size=\\d{1,2}", "size=4")).withRel("thumbnail_large"));
                }
                else {
                    links.add(new Link(thumbnailUrl).withRel("thumbnail_large"));
                }
            }
        }

        return links;
    }
}