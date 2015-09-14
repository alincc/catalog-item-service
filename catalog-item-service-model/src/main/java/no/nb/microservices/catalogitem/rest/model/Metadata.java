package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Metadata {
    private String compositeTitle;
    private TitleInfo titleInfo;
    private TitleInfo alternativeTitleInfo;
    private String typeOfResource;
    private String summary;
    private List<Person> people;
    private Geographic geographic;
    private OriginInfo originInfo;
    private RecordInfo recordInfo;
    private Classification classification;

    public String getCompositeTitle() {
        return compositeTitle;
    }
    
    public void setCompositeTitle(String compositeTitle) {
        this.compositeTitle = compositeTitle;
    }
    
    public TitleInfo getTitleInfo() {
        return titleInfo;
    }

    public void setTitleInfo(TitleInfo titleInfo) {
        this.titleInfo = titleInfo;
    }

    public TitleInfo getAlternativeTitleInfo() {
        return alternativeTitleInfo;
    }
    
    public void setAlternativeTitleInfo(TitleInfo alternativeTitleInfo) {
        this.alternativeTitleInfo = alternativeTitleInfo;
    }
    
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public Geographic getGeographic() {
        return geographic;
    }

    public void setGeographic(Geographic geographic) {
        this.geographic = geographic;
    }

    public OriginInfo getOriginInfo() {
        return originInfo;
    }

    public void setOriginInfo(OriginInfo originInfo) {
        this.originInfo = originInfo;
    }
    
    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public RecordInfo getRecordInfo() {
        return recordInfo;
    }

    public void setRecordInfo(RecordInfo recordInfo) {
        this.recordInfo = recordInfo;
    }

    public String getTypeOfResource() {
        return typeOfResource;
    }

    public void setTypeOfResource(String typeOfResource) {
        this.typeOfResource = typeOfResource;
    }
}
