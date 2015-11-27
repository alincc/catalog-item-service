package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Abstract;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Note;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.Location;
import no.nb.microservices.catalogsearchindex.SearchResource;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class MetadataBuilder {

    private FieldResource field;
    private Mods mods;
    private SearchResource searchResource;
    
    public MetadataBuilder withItem(Item item) {
        this.field = item.getField();
        this.mods = item.getMods();
        this.searchResource = item.getSearchResource();
        return this;
    }
    
    public Metadata build() {
        Metadata metadata = new Metadata();
        metadata.setTitleInfos(new TitleInfosBuilder()
            .withTitleInfos(mods.getTitleInfos())
            .build());

        metadata.setPeople(new NamesBuilder()
                .withNames(mods.getNames())
                .buildPersonList());
        metadata.setCorporates(new NamesBuilder()
                .withNames(mods.getNames())
                .buildCorporatesList());
        metadata.setOriginInfo(new OriginInfoBuilder().withOriginInfo(mods.getOriginInfo()).withItemResource(getItemResource()).build());
        metadata.setGeographic(new GeographicBuilder().withOriginInfo(mods.getOriginInfo()).withLocation(getLocation()).build());
        metadata.setClassification(new ClassificationBuilder().withClassifications(mods.getClassifications()).build());
        metadata.setIdentifiers(new IdentifiersBuilder()
                .withIdentifiers(mods.getIdentifiers())
                .build());
        metadata.setRecordInfo(new RecordInfoBuilder().withRecordInfo(mods.getRecordInfo()).build());
        metadata.setSubject(new SubjectBuilder().withSubjects(mods.getSubjects()).build());
        metadata.setMediaTypes(getMediaTypes());
        metadata.setSummary(getSummary());
        metadata.setTypeOfResource(getTypeOfResource());
        metadata.setGenre(getGenre());
        metadata.setNotes(getNotes(getNotesPredicate()));
        metadata.setStatementOfResponsibility(getNotes(getStatementOfResponsibilityPredicate()));
        metadata.setLanguages(new LanguageBuilder(mods).build());
        metadata.setPageCount(getPageCount());
        
        StreamingInfoStrategy streamingInfoStrategy = StreamingInfoFactory.getStreamingInfoStrategy(getFirstMediatype());
        metadata.setStreamingInfo(streamingInfoStrategy.getStreamingInfo(mods));

        return metadata;
    }

    private int getPageCount() {
        if (searchResource != null && !searchResource.getEmbedded().getItems().isEmpty()){
            return searchResource.getEmbedded().getItems().get(0).getPageCount();
        }
        else {
            return 0;
        }
    }

    private Location getLocation() {
        if (searchResource != null && !searchResource.getEmbedded().getItems().isEmpty()) {
            return searchResource.getEmbedded().getItems().get(0).getLocation();
        } else {
            return null;
        }
    }

    private ItemResource getItemResource() {
        if (searchResource != null && !searchResource.getEmbedded().getItems().isEmpty()) {
            return searchResource.getEmbedded().getItems().get(0);
        }
        return null;
    }

    private String getFirstMediatype() {
        List<String> mediaTypes = getMediaTypes();
        if (mediaTypes != null && !mediaTypes.isEmpty()) {
            return mediaTypes.get(0);
        } else {
            return null;
        }
    }
    
    private List<String> getMediaTypes() {
        if (field != null) {
            return field.getMediaTypes();
        } else {
            return Collections.emptyList();
        }
    }
    
    private String getSummary() {
        if (mods != null) {
            List<Abstract> abstracts = mods.getAbstracts();
            return (abstracts != null && !abstracts.isEmpty()) ? abstracts.get(0).getValue() : null;
        } else {
            return null;
        }
    }

    private String getTypeOfResource() {
        if (mods != null) {
            return mods.getTypeOfResource();
        } else {
            return null;
        }
    }

    private String getGenre() {
        if (mods != null) {
            return mods.getGenre();
        } else {
            return null;
        }
    }

    private List<String> getNotes(Predicate<? super Note> predicate) {
        if (mods != null && mods.getNotes() != null) {
            return mods.getNotes()
                    .stream().filter(predicate)
                    .map(q -> q.getValue())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private Predicate<? super Note> getNotesPredicate() {
        return q -> q.getType() == null;
    }
    
    private Predicate<? super Note> getStatementOfResponsibilityPredicate() {
        return q -> "statement of responsibility".equalsIgnoreCase(q.getType());
    }

}
