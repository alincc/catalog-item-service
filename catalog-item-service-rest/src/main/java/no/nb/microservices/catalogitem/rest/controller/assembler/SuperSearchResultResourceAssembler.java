package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.rest.controller.SearchController;
import no.nb.microservices.catalogitem.rest.controller.SearchResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.SuperItemSearchResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class SuperSearchResultResourceAssembler extends ResourceAssemblerSupport<SuperSearchAggregated, SuperItemSearchResource> {

    public SuperSearchResultResourceAssembler() {
        super(SearchController.class, SuperItemSearchResource.class);
    }

    @Override
    public SuperItemSearchResource toResource(SuperSearchAggregated superSearchAggregated) {
        SuperItemSearchResource resource = new SuperItemSearchResource();

        Map<String, SearchAggregated> searchAggregateds = superSearchAggregated.getSearchAggregateds();
        for (Map.Entry<String, SearchAggregated> entry : searchAggregateds.entrySet()) {
            ItemSearchResource itemSearchResource = new SearchResultResourceAssembler().toResource(entry.getValue());
            final String mediaType = entry.getKey();
            if ("bøker".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setBooks(itemSearchResource);
            } else if ("bilder".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setImages(itemSearchResource);
            } else if ("artikler".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setJournals(itemSearchResource);
            } else if ("kart".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setMaps(itemSearchResource);
            } else if ("film".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setMovies(itemSearchResource);
            } else if ("noter".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setMusicBooks(itemSearchResource);
            } else if ("musikk".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setMusic(itemSearchResource);
            } else if ("musikkmanuskripter".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setMusicManuscripts(itemSearchResource);
            } else if ("aviser".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setNewspapers(itemSearchResource);
            } else if ("other".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setOthers(itemSearchResource);
            } else if ("plakater".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setPosters(itemSearchResource);
            } else if ("programrapporter".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setProgramReports(itemSearchResource);
            } else if ("privatarkivmateriale".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setPrivateArchiveMaterial(itemSearchResource);
            } else if ("radio".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setRadio(itemSearchResource);
            } else if ("lydopptak".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setSoundRecords(itemSearchResource);
            } else if ("fjernsyn".equalsIgnoreCase(mediaType)) {
                resource.getEmbedded().setTelevision(itemSearchResource);
            }
        }

        UriTemplate base = new UriTemplate(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        Link link = new Link(new UriTemplate(builder.build().toString()), Link.REL_SELF);
        resource.add(link);

        return resource;
    }
}
