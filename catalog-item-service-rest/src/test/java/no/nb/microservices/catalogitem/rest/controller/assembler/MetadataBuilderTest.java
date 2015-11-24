package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nb.microservices.catalogsearchindex.EmbeddedWrapper;
import no.nb.microservices.catalogsearchindex.ItemResource;
import org.junit.Test;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Abstract;
import no.nb.microservices.catalogmetadata.model.mods.v3.Language;
import no.nb.microservices.catalogmetadata.model.mods.v3.LanguageTerm;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Note;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Place;
import no.nb.microservices.catalogmetadata.model.mods.v3.Subject;
import no.nb.microservices.catalogmetadata.model.mods.v3.Topic;
import no.nb.microservices.catalogmetadata.test.model.fields.TestFields;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import no.nb.microservices.catalogsearchindex.SearchResource;

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
        FieldResource fields = new FieldResource();
        fields.setMediaTypes(createMediaTypes());

        SearchResource searchResource = new SearchResource();
        searchResource.setEmbedded(createWrapper());

        Item item = new Item.ItemBuilder(id).mods(mods).fields(fields).withSearchResource(searchResource).hasAccess(true).build();
        
        Metadata metadata = new MetadataBuilder(item).build();

        assertEquals("Bøker",  metadata.getMediaTypes().get(0));
        assertEquals("Aviser", metadata.getMediaTypes().get(1));
        assertTrue("Should have titles", !metadata.getTitleInfos().isEmpty());
        assertNotNull("Should have placeString", metadata.getGeographic().getPlaceString());
        assertEquals("It's a summary", metadata.getSummary());
        assertEquals("still image", metadata.getTypeOfResource());
        assertEquals("drama", metadata.getGenre());
        assertEquals(1, metadata.getNotes().size());
        assertNotNull("Should have subject", metadata.getSubject());
        assertNotNull("Should have Statement Of Responsibility", metadata.getStatementOfResponsibility());
        assertTrue("Should have language", metadata.getLanguages().equals(Arrays.asList("nob", "eng")));
        assertEquals(70, metadata.getPageCount());
    }

    private EmbeddedWrapper createWrapper() {
        EmbeddedWrapper embeddedWrapper = new EmbeddedWrapper();
            embeddedWrapper.setItems(createItem());
        return embeddedWrapper;
    }

    private List<ItemResource> createItem() {
        List<ItemResource> itemResources = new ArrayList<>();
        ItemResource pageCount = new ItemResource();
        pageCount.setPageCount(70);
        itemResources.add(pageCount);
        return itemResources;
    }

    @Test
    public void testStreamingInfo() {
        Mods mods = TestMods.aDefaultRadioProgramMods().build();
        FieldResource fields = TestFields.aDefaultRadio().build();
        Item item = new Item.ItemBuilder("id1")
                .mods(mods)
                .fields(fields)
                .build();
        
        Metadata metadata = new MetadataBuilder(item).build();
        
        assertNotNull("Should have a streamingInfo", metadata.getStreamingInfo());
    }

    @Test
    public void testMediatypeWithoutStreamingInfo() {
        Mods mods = TestMods.aDefaultBookMods().build();
        FieldResource fields = TestFields.aDefaultBook().build();
        Item item = new Item.ItemBuilder("id1")
                .mods(mods)
                .fields(fields)
                .build();
        
        Metadata metadata = new MetadataBuilder(item).build();
        
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
