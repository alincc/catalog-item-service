package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Language;

/**
 * Created by rolfm on 30.09.15.
 */
public class LanguageBuilder {


    private List<Language> languages;

    public LanguageBuilder(final Mods mods) {
        if (mods == null) {
            this.languages = Arrays.asList(new Language());
        } else {
            this.languages = mods.getLanguages() != null ? mods.getLanguages() : Arrays.asList(new Language());
        }
    }

    public no.nb.microservices.catalogitem.rest.model.Language build() {
        no.nb.microservices.catalogitem.rest.model.Language language = new no.nb.microservices.catalogitem.rest.model.Language();
        language.setLanguageTerm(getLanguageTerm());

        return language;
    }

    private List<String> getLanguageTerm() {
        List<String> languageTerm = new ArrayList<>();
        if (languages != null && !languages.isEmpty()) {
            for (Language language : languages.stream().filter(q -> q.getLanguageTerm() != null).collect(Collectors.toList())) {
                languageTerm.addAll(language.getLanguageTerm().stream().map(q -> q.getValue()).collect(Collectors.toList()));
            }
        }

        return languageTerm;
    }

}