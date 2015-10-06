package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Language;
import no.nb.microservices.catalogmetadata.model.mods.v3.LanguageTerm;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageBuilderTest {

    @Test
    public void languageLanguageTermTest() {
        Mods mods = new Mods();
        mods.setLanguages(createLanguages("eng", "mul", "nob"));

        List<String> languages = new LanguageBuilder(mods).build();

        assertTrue(languages.equals(Arrays.asList("eng", "mul", "nob")));
    }

    @Test
    public void languageLanguageTermEmptyTest() {
        Mods mods = new Mods();
        mods.setLanguages(null);
        
        List<String> languages = new LanguageBuilder(mods).build();

        assertNull(languages);
    }

    private List<Language> createLanguages(String... codes) {
        List<Language> result = new ArrayList<>();
        for(String code : codes) {
            Language language = new Language();
            LanguageTerm term = new LanguageTerm();
            term.setType("code");
            term.setValue(code);
            term.setAuthority("iso639-2b");
            language.setLanguageTerms(Arrays.asList(term));
            result.add(language);
        }
        return result;
    }

}
