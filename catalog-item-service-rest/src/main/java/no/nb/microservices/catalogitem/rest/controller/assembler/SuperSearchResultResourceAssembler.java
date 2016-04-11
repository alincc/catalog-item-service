package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.rest.controller.SearchController;
import no.nb.microservices.catalogitem.rest.controller.SearchResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.SuperEmbeddedWrapper;
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
        SuperEmbeddedWrapper embeddedWrapper = getSuperEmbeddedWrapper(superSearchAggregated);
        SuperItemSearchResource resource = new SuperItemSearchResource(superSearchAggregated.getMetadata(), embeddedWrapper);
        resource.add(getSelfLink());
        return resource;
    }

    private SuperEmbeddedWrapper getSuperEmbeddedWrapper(SuperSearchAggregated superSearchAggregated) {
        SuperEmbeddedWrapper embeddedWrapper = new SuperEmbeddedWrapper();
        Map<String, SearchAggregated> searchAggregateds = superSearchAggregated.getSearchAggregateds();
        for (Map.Entry<String, SearchAggregated> entry : searchAggregateds.entrySet()) {
            ItemSearchResource itemSearchResource = new SearchResultResourceAssembler().toResource(entry.getValue());
            final String mediaType = entry.getKey();
            if ("bøker".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setBooks(itemSearchResource);
            } else if ("bilder".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setImages(itemSearchResource);
            } else if ("artikler".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setJournals(itemSearchResource);
            } else if ("kart".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setMaps(itemSearchResource);
            } else if ("film".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setMovies(itemSearchResource);
            } else if ("noter".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setMusicBooks(itemSearchResource);
            } else if ("musikk".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setMusic(itemSearchResource);
            } else if ("musikkmanuskripter".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setMusicManuscripts(itemSearchResource);
            } else if ("aviser".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setNewspapers(itemSearchResource);
            } else if ("other".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setOthers(itemSearchResource);
            } else if ("plakater".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setPosters(itemSearchResource);
            } else if ("programrapporter".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setProgramReports(itemSearchResource);
            } else if ("privatarkivmateriale".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setPrivateArchiveMaterial(itemSearchResource);
            } else if ("radio".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setRadio(itemSearchResource);
            } else if ("lydopptak".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setSoundRecords(itemSearchResource);
            } else if ("fjernsyn".equalsIgnoreCase(mediaType)) {
                embeddedWrapper.setTelevision(itemSearchResource);
            }
        }
        return embeddedWrapper;
    }

    private Link getSelfLink() {
        UriTemplate base = new UriTemplate(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        return new Link(new UriTemplate(builder.build().toString()), Link.REL_SELF);
    }
}
