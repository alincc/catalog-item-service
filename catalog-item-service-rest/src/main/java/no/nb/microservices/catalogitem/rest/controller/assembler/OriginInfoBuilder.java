package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogitem.rest.model.OriginInfo;

public class OriginInfoBuilder {

    private no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo originInfo;
    
    public OriginInfoBuilder mods(final Mods mods) {
        if (mods == null) {
            this.originInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo();
        } else {
            this.originInfo = mods.getOriginInfo() != null ? mods.getOriginInfo() : new no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo();
        }
        return this;
    }
    
    public OriginInfo build() {
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPublisher(getPublisher());
        originInfo.setCaptured(getDateCaptured());
        originInfo.setCreated(getDateCreated());
        originInfo.setEdition(getEdition());
        originInfo.setFrequency(getFrequency());
        originInfo.setIssued(getDateIssued());
        originInfo.setModified(getDateModified());
        
        return originInfo;
    }
    
    private String getPublisher() {
        if (originInfo.getPublisher() != null) {
            return originInfo.getPublisher();
        }
        return null;
    }
    
    private String getDateCaptured() {
        if (originInfo.getDateCaptured() != null) {
            return originInfo.getDateCaptured().getValue();
        }
        return null;
    }
    
    private String getDateCreated() {
        if (originInfo.getDateCreated() != null && !originInfo.getDateCreated().isEmpty()) {
            for (DateMods dateMods : originInfo.getDateCreated()) {
                if ("yes".equalsIgnoreCase(dateMods.getKeyDate())) {
                    return dateMods.getValue();
                }
            }
        }
        return null;
    }        
    
    private String getEdition() {
        if (originInfo.getEdition() != null) {
            return originInfo.getEdition();
        }
        return null;
    }
    private String getFrequency() {
        if (originInfo.getFrequency() != null) {
            return originInfo.getFrequency();
        }
        return null;
    }
    private String getDateIssued() {
        if (originInfo.getDateIssuedList() != null && !originInfo.getDateIssuedList().isEmpty()) {
            for (DateMods dateMods : originInfo.getDateIssuedList()) {
                if (dateMods.getEncoding() == null && dateMods.getPoint() == null) {
                    return dateMods.getValue();
                }
            }
        }
        return null;
    }
    
    private String getDateModified() {
        if (originInfo.getDateModified() != null) {
            return originInfo.getDateModified().getValue();
        }
        return null;
    }
    
}
