package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogmetadata.model.mods.v3.Abstract;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Note;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.Location;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class MetadataBuilder {
    private Mods mods;
    private ItemResource itemResource;
    private boolean simplified;

    public MetadataBuilder withItem(Item item) {
        this.mods = item.getMods();
        this.itemResource = item.getItemResource();
        this.simplified = true;
        return this;
    }

    public MetadataBuilder withExpand() {
        this.simplified = false;
        return this;
    }
    
    public Metadata build() {
        if (this.simplified) {
            return buildSimplified();
        }
        return buildFull();
    }

    public Metadata buildSimplified() {
        Metadata metadata = new Metadata();
        metadata.setTitle(getTitle());
        metadata.setMediaTypes(getMediaTypes());
        metadata.setCreators(getCreators());
        metadata.setOriginInfo(new OriginInfoBuilder().withItemResource(getItemResource()).build());
        metadata.setIdentifiers(new IdentifiersBuilder().withItemResource(itemResource).build());
        metadata.setContentClasses(getContentClasses());
        metadata.setMetadataClasses(getMetadataClasses());
        return metadata;
    }

    private List<String> getMetadataClasses() {
        if (itemResource != null) {
            return itemResource.getMetadataClasses();
        } else {
            return null;
        }
    }

    private List<String> getContentClasses() {
        if (itemResource != null) {
            return itemResource.getContentClasses();
        } else {
            return null;
        }
    }

    public Metadata buildFull() {
        Metadata metadata = new Metadata();
        metadata.setTitle(getTitle());
        metadata.setTitleInfos(new TitleInfosBuilder()
                .withTitleInfos(mods.getTitleInfos())
                .build());

        metadata.setPeople(new NamesBuilder()
                .withNames(mods.getNames())
                .buildPersonList());
        metadata.setCorporates(new NamesBuilder()
                .withNames(mods.getNames())
                .buildCorporatesList());
        metadata.setOriginInfo(new OriginInfoBuilder().withOriginInfo(mods.getOriginInfo()).withItemResource(getItemResource()).withExpand().build());
        metadata.setGeographic(new GeographicBuilder().withOriginInfo(mods.getOriginInfo()).withLocation(getLocation()).build());
        metadata.setClassification(new ClassificationBuilder().withClassifications(mods.getClassifications()).build());
        metadata.setIdentifiers(new IdentifiersBuilder()
                .withIdentifiers(mods.getIdentifiers())
                .withExpand()
                .build());
        metadata.setRecordInfo(new RecordInfoBuilder().withRecordInfo(mods.getRecordInfo()).build());
        metadata.setSubject(new SubjectBuilder().withSubjects(mods.getSubjects()).build());
        metadata.setSummary(getSummary());
        metadata.setTypeOfResource(getTypeOfResource());
        metadata.setGenre(getGenre());
        metadata.setNotes(getNotes(getNotesPredicate()));
        metadata.setStatementOfResponsibility(getNotes(getStatementOfResponsibilityPredicate()));
        metadata.setLanguages(new LanguageBuilder(mods).build());
        metadata.setPageCount(getPageCount());
        metadata.setMediaTypes(getMediaTypes());
        metadata.setCreators(getCreators());
        metadata.setContentClasses(getContentClasses());
        metadata.setMetadataClasses(getMetadataClasses());

        StreamingInfoStrategy streamingInfoStrategy = StreamingInfoFactory.getStreamingInfoStrategy(getFirstMediatype());
        metadata.setStreamingInfo(streamingInfoStrategy.getStreamingInfo(mods));

        return metadata;
    }

    private ItemResource getItemResource() {
        if (itemResource == null) {
            return new ItemResource();
        } else {
            return itemResource;
        }
    }

    public String getTitle() {
        if (itemResource != null) {
            return itemResource.getTitle();
        }
        return null;
    }

    private Location getLocation() {
        return getItemResource().getLocation();
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

    private Integer getPageCount() {
        if (getItemResource() != null){
            return getItemResource().getPageCount();
        }
        else {
            return 0;
        }
    }

    private List<String> getMediaTypes() {
        return getItemResource().getMediaTypes();
    }

    private List<String> getCreators() {
        if (getItemResource() != null) {
            return getItemResource().getCreators();
        }
        return Collections.emptyList();
    }

    private String getFirstMediatype() {
        List<String> mediaTypes = getMediaTypes();
        if (mediaTypes != null && !mediaTypes.isEmpty()) {
            return mediaTypes.get(0);
        } else {
            return null;
        }
    }
}