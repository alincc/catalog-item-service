package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    private String title;
    private List<TitleInfo> titleInfos;
    private String typeOfResource;
    private String genre;
    private String summary;
    private List<Person> people;
    private List<Corporates> corporates;
    private Geographic geographic;
    private OriginInfo originInfo;
    private RecordInfo recordInfo;
    private Classification classification;
    private Identifiers identifiers;
    private List<String> notes;
    private Subject subject;
    private List<String> statementOfResponsibility;
    private List<String> languages;
    private List<StreamingInfo> streamingInfo;
    private Integer pageCount;
    private List<String> mediaTypes;
    private List<String> creators;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TitleInfo> getTitleInfos() {
        if (titleInfos == null) {
            return Collections.emptyList();
        } else {
            return titleInfos;
        }
    }

    public void setTitleInfos(List<TitleInfo> titleInfos) {
        this.titleInfos = titleInfos;
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

    public List<Corporates> getCorporates() {
        return corporates;
    }

    public void setCorporates(List<Corporates> corporates) {
        this.corporates = corporates;
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

    public Identifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Identifiers identifiers) {
        this.identifiers = identifiers;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<String> getStatementOfResponsibility() {
        return statementOfResponsibility;
    }

    public void setStatementOfResponsibility(List<String> statementOfResponsibility) {
        this.statementOfResponsibility = statementOfResponsibility; 
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<StreamingInfo> getStreamingInfo() {
        return streamingInfo;
    }

    public void setStreamingInfo(List<StreamingInfo> streamingInfo) {
        this.streamingInfo = streamingInfo;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<String> getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(List<String> mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public List<String> getCreators() {
        return creators;
    }

    public void setCreators(List<String> creators) {
        this.creators = creators;
    }
}