package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Url;
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
        return links;

//        String thumbnailUrl = "";
//        boolean isEbook = item.getId().contains("digiebok");
//        boolean isBook = item.getField().getMediaTypes().stream().filter(q -> q.equalsIgnoreCase("bøker")).findAny().isPresent();
//        boolean isPrivateArchive = item.getField().getMediaTypes().stream().filter(q -> q.equalsIgnoreCase("privatarkivmateriale")).findAny().isPresent();
//        boolean isMusicManuscript = item.getField().getMediaTypes().stream().filter(q -> q.equalsIgnoreCase("musikkmanuskripter")).findAny().isPresent();
//        boolean isJp2 = item.getField().getContentClasses().stream().filter(q -> q.equalsIgnoreCase("jp2")).findAny().isPresent();
//
//        if  (item.getMods() != null && item.getMods().getLocation() != null && item.getMods().getLocation().getUrls() != null && !item.getMods().getLocation().getUrls().isEmpty()) {
//            String location = (item.getMods().getLocation().getUrls().stream()
//                    .filter(q -> q.getAccess() != null && q.getAccess().equalsIgnoreCase("preview")).findAny()
//                    .orElseGet(() -> item.getMods().getLocation().getUrls().get(0))).getValue();
//            thumbnailUrl = location.startsWith("http") ? location : "";
//        }
//
//        if (StringUtils.isNotEmpty(thumbnailUrl)) {
//            if (thumbnailUrl.contains("gallerinor")) {
//                links.add(new Link(thumbnailUrl.replaceAll("size=\\d{1,2}", "size=16")).withRel("thumbnail_small"));
//                links.add(new Link(thumbnailUrl.replaceAll("size=\\d{1,2}", "size=4")).withRel("thumbnail_large"));
//            }
//            else {
//                links.add(new Link(thumbnailUrl).withRel("thumbnail_large"));
//            }
//        } else if (isJp2) {
//            String urn = item.getId();
//
//            if (isBook && !isEbook) {
//
//            }
//            else if (isPrivateArchive || isMusicManuscript || isEbook) {
//
//            }
//
//            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getId(), 256).withRel("thumbnail_large"));
//            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getId(), 128).withRel("thumbnail_medium"));
//            links.add(ResourceLinkBuilder.linkTo(ResourceTemplateLink.THUMBNAIL, item.getId(), 64).withRel("thumbnail_small"));
//        }
//
//        return links;
    }
}
