package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuperEmbeddedWrapper {
    private ItemSearchResource books;
    private ItemSearchResource images;
    private ItemSearchResource journals;
    private ItemSearchResource maps;
    private ItemSearchResource movies;
    private ItemSearchResource musicBooks;
    private ItemSearchResource music;
    private ItemSearchResource musicManuscripts;
    private ItemSearchResource newspapers;
    private ItemSearchResource others;
    private ItemSearchResource posters;
    private ItemSearchResource privateArchiveMaterial;
    private ItemSearchResource programReports;
    private ItemSearchResource radio;
    private ItemSearchResource soundRecords;
    private ItemSearchResource television;

    public ItemSearchResource getBooks() {
        return books;
    }

    public void setBooks(ItemSearchResource books) {
        this.books = books;
    }

    public ItemSearchResource getImages() {
        return images;
    }

    public void setImages(ItemSearchResource images) {
        this.images = images;
    }

    public ItemSearchResource getJournals() {
        return journals;
    }

    public void setJournals(ItemSearchResource journals) {
        this.journals = journals;
    }

    public ItemSearchResource getMaps() {
        return maps;
    }

    public void setMaps(ItemSearchResource maps) {
        this.maps = maps;
    }

    public ItemSearchResource getMovies() {
        return movies;
    }

    public void setMovies(ItemSearchResource movies) {
        this.movies = movies;
    }

    public ItemSearchResource getMusicBooks() {
        return musicBooks;
    }

    public void setMusicBooks(ItemSearchResource musicBooks) {
        this.musicBooks = musicBooks;
    }

    public ItemSearchResource getMusic() {
        return music;
    }

    public void setMusic(ItemSearchResource music) {
        this.music = music;
    }

    public ItemSearchResource getMusicManuscripts() {
        return musicManuscripts;
    }

    public void setMusicManuscripts(ItemSearchResource musicManuscripts) {
        this.musicManuscripts = musicManuscripts;
    }

    public ItemSearchResource getNewspapers() {
        return newspapers;
    }

    public void setNewspapers(ItemSearchResource newspapers) {
        this.newspapers = newspapers;
    }

    public ItemSearchResource getOthers() {
        return others;
    }

    public void setOthers(ItemSearchResource others) {
        this.others = others;
    }

    public ItemSearchResource getPosters() {
        return posters;
    }

    public void setPosters(ItemSearchResource posters) {
        this.posters = posters;
    }

    public ItemSearchResource getPrivateArchiveMaterial() {
        return privateArchiveMaterial;
    }

    public void setPrivateArchiveMaterial(ItemSearchResource privateArchiveMaterial) {
        this.privateArchiveMaterial = privateArchiveMaterial;
    }

    public ItemSearchResource getProgramReports() {
        return programReports;
    }

    public void setProgramReports(ItemSearchResource programReports) {
        this.programReports = programReports;
    }

    public ItemSearchResource getRadio() {
        return radio;
    }

    public void setRadio(ItemSearchResource radio) {
        this.radio = radio;
    }

    public ItemSearchResource getSoundRecords() {
        return soundRecords;
    }

    public void setSoundRecords(ItemSearchResource soundRecords) {
        this.soundRecords = soundRecords;
    }

    public ItemSearchResource getTelevision() {
        return television;
    }

    public void setTelevision(ItemSearchResource television) {
        this.television = television;
    }
}
