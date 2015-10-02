package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Language;
import no.nb.microservices.catalogmetadata.model.mods.v3.LanguageTerm;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by rolfm on 01.10.15.
 */
public class LanguageBuilderTest {

    @Test
    public void languageLanguageTermTest() {
        Mods mods = new Mods();
        mods.setLanguages(createLanguages());

        LanguageBuilder builder = new LanguageBuilder(mods);
        no.nb.microservices.catalogitem.rest.model.Language build = builder.build();

        assertEquals(3, build.getLanguageTerm().size());
        assertEquals("eng", getLanguageTermFromResult(build, "eng"));
        assertEquals("mul", getLanguageTermFromResult(build, "mul"));
        assertEquals("nob", getLanguageTermFromResult(build, "nob"));

    }

    @Test
    public void languageLanguageTermEmptyTest() {
        Mods mods = new Mods();
        mods.setLanguages(null);
        LanguageBuilder builder = new LanguageBuilder(mods);

        no.nb.microservices.catalogitem.rest.model.Language build = builder.build();

        assertEquals(0, build.getLanguageTerm().size());
    }

    private List<Language> createLanguages() {
        Language language1 = new Language();
        language1.setLanguageTerms(createLanguageTerms().subList(0, 2));
        Language language2 = new Language();
        language2.setLanguageTerms(createLanguageTerms().subList(2, 3));
        return Arrays.asList(language1, language2);
    }

    private List<LanguageTerm> createLanguageTerms() {
        LanguageTerm languageTerm1 = new LanguageTerm();
        languageTerm1.setValue("eng");
        LanguageTerm languageTerm2 = new LanguageTerm();
        languageTerm2.setValue("mul");
        LanguageTerm languageTerm3 = new LanguageTerm();
        languageTerm3.setValue("nob");
        return Arrays.asList(languageTerm1, languageTerm2, languageTerm3);
    }

    private String getLanguageTermFromResult(no.nb.microservices.catalogitem.rest.model.Language build, String languageTerm) {
        return build.getLanguageTerm().stream().filter(q -> q.equalsIgnoreCase(languageTerm)).findFirst().get();
    }

}
