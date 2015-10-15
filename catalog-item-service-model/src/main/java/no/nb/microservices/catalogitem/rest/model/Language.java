package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Language {

    private List<String> languageTerm;

    public Language() {
    }

    public Language(List<String> languageTerm) {
        this.languageTerm = languageTerm;
    }

    public List<String> getLanguageTerm() {
        return languageTerm;
    }

    public void setLanguageTerm(List<String> languageTerm) {
        this.languageTerm = languageTerm;
    }
}

