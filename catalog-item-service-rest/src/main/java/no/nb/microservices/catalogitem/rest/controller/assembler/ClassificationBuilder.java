package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogitem.rest.model.Classification;

public class ClassificationBuilder {

    private List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications;
    
    public ClassificationBuilder withClassifications(List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications) {
        this.classifications = classifications;
        return this;
    }

    public Classification build() {
        if (classifications == null) {
            return null;
        }

        Classification classification = new Classification(getDdc(), getUdc());
        return classification.isEmpty() ? null : classification;
     }
     
    private List<String> getDdc() {
        return classifications.stream()
                .filter(authority -> "ddc".equalsIgnoreCase(authority.getAuthority()))
                .map(classification -> classification.getValue())
                .collect(Collectors.toList());
    }
    
    private List<String> getUdc() {
        return classifications.stream()
                .filter(authority -> "udc".equalsIgnoreCase(authority.getAuthority()))
                .map(classification -> classification.getValue())
                .collect(Collectors.toList());
    }
    
}
