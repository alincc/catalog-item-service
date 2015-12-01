package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogsearchindex.ItemResource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public class ThumbnailBuilder {
    public static final String THUMBNAIL_LARGE = "thumbnail_large";
    public static final String THUMBNAIL_MEDIUM = "thumbnail_medium";
    public static final String THUMBNAIL_SMALL = "thumbnail_small";
    private ItemResource itemResource;
    private Mods mods;

    public ThumbnailBuilder() {
        super();
    }

    public ThumbnailBuilder withItemResource(ItemResource itemResource) {
        this.itemResource = itemResource;
        return this;
    }
    
    public ThumbnailBuilder withMods(Mods mods) {
        this.mods = mods;
        return this;
    }

    public List<Link> build() {
        List<Link> links = new ArrayList<>();

        if (StringUtils.isNotEmpty(itemResource.getThumbnailUrn())) {
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, itemResource.getThumbnailUrn(), 256).withRel(THUMBNAIL_LARGE));
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, itemResource.getThumbnailUrn(), 128).withRel(THUMBNAIL_MEDIUM));
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, itemResource.getThumbnailUrn(), 64).withRel(THUMBNAIL_SMALL));
        }
        else if (mods != null
                && mods.getLocation() != null
                && mods.getLocation().getUrls() != null
                && !mods.getLocation().getUrls().isEmpty())
        {
            String thumbnailUrl = "";
            String location = (mods.getLocation().getUrls().stream()
                    .filter(q -> "preview".equalsIgnoreCase(q.getAccess())).findAny()
                    .orElseGet(() -> mods.getLocation().getUrls().get(0))).getValue();
            thumbnailUrl = location.startsWith("http://") ? location : "";

            if (StringUtils.isNotEmpty(thumbnailUrl)) {
                if (thumbnailUrl.contains("gallerinor")) {
                    links.add(new Link(thumbnailUrl.replaceAll("size=\\d{1,2}", "size=16")).withRel(THUMBNAIL_SMALL));
                    links.add(new Link(thumbnailUrl.replaceAll("size=\\d{1,2}", "size=4")).withRel(THUMBNAIL_LARGE));
                }
                else {
                    links.add(new Link(thumbnailUrl).withRel(THUMBNAIL_LARGE));
                }
            }
        }

        return links;
    }
}
