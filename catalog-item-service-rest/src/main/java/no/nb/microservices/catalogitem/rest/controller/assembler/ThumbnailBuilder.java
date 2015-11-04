package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public class ThumbnailBuilder {
    public static final String THUMBNAIL_LARGE = "thumbnail_large";
    public static final String THUMBNAIL_MEDIUM = "thumbnail_medium";
    public static final String THUMBNAIL_SMALL = "thumbnail_small";
    private final Item item;

    public ThumbnailBuilder(Item item) {
        this.item = item;
    }

    public List<Link> build() {
        List<Link> links = new ArrayList<>();

        if (StringUtils.isNotEmpty(item.getField().getThumbnailUrl())) {
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getField().getThumbnailUrl(), 256).withRel(THUMBNAIL_LARGE));
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getField().getThumbnailUrl(), 128).withRel(THUMBNAIL_MEDIUM));
            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getField().getThumbnailUrl(), 64).withRel(THUMBNAIL_SMALL));
        }
        else if (item.getMods() != null
                && item.getMods().getLocation() != null
                && item.getMods().getLocation().getUrls() != null
                && !item.getMods().getLocation().getUrls().isEmpty())
        {
            String thumbnailUrl = "";
            String location = (item.getMods().getLocation().getUrls().stream()
                    .filter(q -> q.getAccess() != null && "preview".equalsIgnoreCase(q.getAccess())).findAny()
                    .orElseGet(() -> item.getMods().getLocation().getUrls().get(0))).getValue();
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
