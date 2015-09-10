package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogitem.rest.model.Classification;

public class ClassificationBuilder {

    private List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications;
    
    public ClassificationBuilder(List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications) {
        if (classifications == null) {
            this.classifications = new ArrayList<>();
        } else {
            this.classifications = classifications;
        }
    }

    public Classification build() {
        Classification classification = new Classification();
        
        addDdc(classification);
        addUdc(classification);
        
        return classification;
         
     }
     
     private void addDdc(Classification classification) {
         Iterator<String> iter = getDdc().iterator();
            while (iter.hasNext()) {
                classification.addDdc(iter.next());
            }
     }

     private void addUdc(Classification classification) {
         Iterator<String> iter = getUdc().iterator();
            while (iter.hasNext()) {
                classification.addUdc(iter.next());
            }
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
