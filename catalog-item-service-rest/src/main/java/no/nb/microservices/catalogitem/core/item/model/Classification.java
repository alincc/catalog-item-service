package no.nb.microservices.catalogitem.core.item.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public final class Classification {

    private List<String> ddc;
    private List<String> udc;
    
    private Classification(Builder builder) {
        this.ddc = builder.getDdc();
        this.udc = builder.getUdc();
    }
    
    public List<String> getDdc() {
        return Collections.unmodifiableList(ddc);
    }

    public List<String> getUdc() {
        return Collections.unmodifiableList(udc);
    }

    public static class Builder {
        
        private List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications;
        
        public Builder(final Mods mods) {
            if (mods != null && mods.getClassifications() != null) {
                this.classifications = mods.getClassifications();
            } else {
                this.classifications = new ArrayList<>();
            }
            
        }

        public Classification build() {
            return new Classification(this);
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

}
