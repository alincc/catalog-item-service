package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogmetadata.model.mods.v3.Language;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class LanguageBuilder {


    private List<Language> languages;

    public LanguageBuilder withMods(Mods mods) {
        if (mods != null) {
            this.languages = mods.getLanguages() != null ? mods.getLanguages() : new ArrayList<>();
        }
        return this;
    }

    public List<String> build() {
        List<String> codes = null;
        for (Language language : languages) {
            if (language.getLanguageTerm() != null) {
                if (codes == null) {
                    codes = new ArrayList<>();
                }
                codes.addAll(language.getLanguageTerm()
                        .stream()
                        .filter(q -> language.getObjectPart() == null 
                            && "code".equalsIgnoreCase(q.getType()) 
                            && "iso639-2b".equals(q.getAuthority()))
                        .map(q -> q.getValue())
                        .collect(Collectors.toList()));
            }
        }        

        return codes;
    }

}