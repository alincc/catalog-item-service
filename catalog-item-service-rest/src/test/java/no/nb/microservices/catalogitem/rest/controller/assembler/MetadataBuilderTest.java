package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogmetadata.model.mods.v3.*;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.TestItemResource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class MetadataBuilderTest {

    @Test
    public void testBuild() {
        String id = "id1";
        Mods mods = new Mods();
        createTitleInfo(mods);
        mods.setOriginInfo(createPlace());
        mods.setAbstracts(createtSummary());

        List<Note> notes = createNotes();
        notes.addAll(createNotesStatement());

        mods.setNotes(notes);
        mods.setTypeOfResource("still image");
        mods.setGenre("drama");
        mods.setSubjects(createSubjects());
        mods.setLanguages(createLanguages("nob", "eng"));

        ItemResource itemResource = new ItemResource();
        itemResource.setPageCount(70);
        itemResource.setMediaTypes(createMediaTypes());
        itemResource.setContentClasses(Arrays.asList("public","jp2"));
        itemResource.setMetadataClasses(Arrays.asList("public","restricted"));

        Item item = new Item.ItemBuilder(id)
                .mods(mods)
                .withItemResource(itemResource)
                .hasAccess(true).build();
        
        Metadata metadata = new MetadataBuilder()
                .withItem(item)
                .withExpand()
                .build();

        assertThat("metadata has titles", metadata.getTitleInfos().size(), is(2));
        assertThat("metadata has placeString", metadata.getGeographic().getPlaceString(), is("Norge;Telemark;Kviteseid;;;;;"));
        assertThat("metadata has summary", metadata.getSummary(), is("It's a summary"));
        assertThat("type of resource is still image", metadata.getTypeOfResource(), is("still image"));
        assertThat("genre is drama", metadata.getGenre(), is("drama"));
        assertThat("metadata has notes", metadata.getNotes().size(), is(1));
        assertThat("metadata has subject", metadata.getSubject(), is(not(nullValue())));
        assertThat("metadata has statement of responsibility", metadata.getStatementOfResponsibility(), is(not(nullValue())));
        assertThat("metadata has language", metadata.getLanguages().size(), is(2));
        assertThat("metadata has pagecount", metadata.getPageCount(), is(70));
        assertThat("metadata has contentClasses", metadata.getContentClasses().size(), is(2));
        assertThat("metadata has metadataClasses", metadata.getMetadataClasses().size(), is(2));
    }

    @Test
    public void testStreamingInfo() {
        Mods mods = TestMods.aDefaultRadioProgramMods().build();
        ItemResource itemResource = TestItemResource.aDefaultRadio().build();
        Item item = new Item.ItemBuilder("id1")
                .mods(mods)
                .withItemResource(itemResource)
                .build();
        
        Metadata metadata = new MetadataBuilder()
                .withItem(item)
                .withExpand()
                .build();
        
        assertNotNull("Should have a streamingInfo", metadata.getStreamingInfo());
    }

    @Test
    public void testMediatypeWithoutStreamingInfo() {
        Mods mods = TestMods.aDefaultBookMods().build();
        ItemResource itemResource = TestItemResource.aDefaultBook().build();
        Item item = new Item.ItemBuilder("id1")
                .mods(mods)
                .withItemResource(itemResource)
                .build();
        
        Metadata metadata = new MetadataBuilder()
                .withItem(item)
                .build();
        
        assertNull("Should not have a streamingInfo", metadata.getStreamingInfo());
    }

    private List<Note> createNotes() {
        Note note1 = new Note();
        note1.setValue("Tittelinformasjon er hentet fra tilhørende dokumentasjonsmateriale. Widerøe Flyfoto A/S solgte i 1983 disse fotografiene til Kviteseid kommune.");
        List<Note> notes = new ArrayList<>();
        notes.add(note1);
       return notes;
    }

    private List<Note> createNotesStatement() {
        Note note1 = new Note();
        note1.setValue("Tittelinformasjon er hentet fra tilhørende dokumentasjonsmateriale. Widerøe Flyfoto A/S solgte i 1983 disse fotografiene til Kviteseid kommune.");
        note1.setType("statement of responsibility");
        List<Note> notes = new ArrayList<>();
        notes.add(note1);
        return notes;
    }

    private List<Language> createLanguages(String... codes) {
        List<Language> languages = new ArrayList<>(); 
        for(String code : codes) {
            Language language = new Language();
            LanguageTerm languageTerm = new LanguageTerm();
            languageTerm.setType("code");
            languageTerm.setValue(code);
            languageTerm.setAuthority("iso639-2b");
            language.setLanguageTerms(Arrays.asList(languageTerm));
            languages.add(language);
        }
        return languages;
    }

    private List<String> createMediaTypes() {
        return Arrays.asList("Bøker", "Aviser");
    }

    private OriginInfo createPlace() {
        Place place = new Place();
        place.setPlaceTerm("Norge;Telemark;Kviteseid;;;;;");
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPlace(place);
        return originInfo;
    }

    private List<Abstract> createtSummary() {
        List<Abstract> abstracts = new ArrayList<>();
        Abstract summary = new Abstract();
        summary.setValue("It's a summary");
        abstracts.add(summary);
        return abstracts;
    }
    
    private void createTitleInfo(Mods mods) {
        mods.setTitleInfos(Arrays.asList(createStandardTitleInfo(), createAlternativeTitleInfo()));
    }

    private no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo createAlternativeTitleInfo() {
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo alternativeTitleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        alternativeTitleInfo.setType("alternative");
        alternativeTitleInfo.setTitle("Supersonic");
        return alternativeTitleInfo;
    }

    private no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo createStandardTitleInfo() {
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo standardTitleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        standardTitleInfo.setTitle("Oasis");
        return standardTitleInfo;
    }
    
    private List<Subject> createSubjects() {
        Subject subject = new Subject();
        subject.setTopic(createTopics());
        return Arrays.asList(subject);
    }

    private List<Topic> createTopics() {
        Topic topic = new Topic();
        topic.setValue("Ski");
        return Arrays.asList(topic);
    }
}