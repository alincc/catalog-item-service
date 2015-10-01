package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Abstract;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Note;

public final class MetadataBuilder {

    private final FieldResource field;
    private final Mods mods;
    
    public MetadataBuilder(Item item) {
        super();
        this.field = item.getField();
        this.mods = item.getMods();
    }

    public Metadata build() {
        Metadata metadata = new Metadata();
        metadata.setCompositeTitle(getTitle());
        
        TitleInfoDirector titleInfoDirector = new TitleInfoDirector();
        TitleInfo standardTitleInfo = titleInfoDirector.createTitleInfo(new StandardTitleInfoBuilder(), mods);
        metadata.setTitleInfo(standardTitleInfo);
        TitleInfo alternativeTitleInfo = titleInfoDirector.createTitleInfo(new AlternativeTitleInfoBuilder(), mods);
        metadata.setAlternativeTitleInfo(alternativeTitleInfo);

        metadata.setPeople(new PersonsBuilder(mods.getNames()).buildList());
        metadata.setOriginInfo(new OriginInfoBuilder().mods(mods).build());
        metadata.setGeographic(new GeographicBuilder(mods.getOriginInfo()).build());
        metadata.setClassification(new ClassificationBuilder(mods.getClassifications()).build());
        metadata.setIdentifiers(new IdentifiersBuilder(field).build());
        metadata.setRecordInfo(new RecordInfoBuilder().mods(mods).build());
        metadata.setSubject(new SubjectBuilder(mods).build());
        metadata.setMediaTypes(getMediaTypes());
        metadata.setSummary(getSummary());
        metadata.setTypeOfResource(getTypeOfResource());
        metadata.setGenre(getGenre());
        metadata.setNotes(getNotes());
        metadata.setStatementOfResponsibility(getStatementOfResponsibility());
        
        return metadata;
    }
    
    private String getTitle() {
        if (field != null) {
            return field.getTitle();
        }
        return null;
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
        }
        else {
            return null;
        }
    }

    private String getGenre() {
        if (mods != null) {
            return mods.getGenre();
        }
        else {
            return null;
        }
    }

    private List<String> getStatementOfResponsibility() {
        if (mods != null && mods.getNotes() != null) {
            return mods.getNotes().stream().filter(q -> (q.getType() != null && q.getType().equalsIgnoreCase("statement of responsibility"))).map(q -> q.getValue()).collect(Collectors.toList());
            }
        else {
            return null;
        }
            
    }

    private List<String> getNotes() {
        if (mods != null && mods.getNotes() != null && mods.getNotes().get(0).getType() == null) {
            return mods.getNotes().stream().filter(q -> q.getType() == null).map(q -> q.getValue()).collect(Collectors.toList());
            }
        else {
            return null;
        }
    }

    private List<String> getMediaTypes() {
        if (field != null) {
            return field.getMediaTypes();
        }
        else {
            return null;
        }
    }
}
