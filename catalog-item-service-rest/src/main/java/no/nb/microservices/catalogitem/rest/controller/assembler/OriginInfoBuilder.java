package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import org.apache.commons.validator.routines.DateValidator;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OriginInfoBuilder {

    private no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo originInfo;
    
    public OriginInfoBuilder withOriginInfo(final no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo originInfo) {
        this.originInfo = originInfo;
        return this;
    }
    
    public OriginInfo build() {
        if (originInfo == null) {
            return null;
        }
        if (getPublisher() != null || getDateIssued() != null || getFrequency() != null
                || getDateCreated() != null || getDateCaptured() != null || getDateCaptured() != null
                || getDateModified() != null || getEdition() != null) {
            OriginInfo originInfo = new OriginInfo(getPublisher(), getDateIssued(), getFrequency(),
                    getDateCreated(), getDateCaptured(), getDateModified(), getEdition());
            return originInfo;
        }
        return null;
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
        String dateIssued = null;
        dateIssued = getEncodedDateValue(d -> "w3cdtf".equals(d.getEncoding()));
        if (dateIssued == null) {
            dateIssued = getEncodedDateValue(d -> "marc".equals(d.getEncoding()));
        }
        if (dateIssued == null) {
            dateIssued = getNotEncodedDateValue();
        }
        return dateIssued;
    }

    private String getEncodedDateValue(Predicate<? super DateMods> predicate) {
        String dateIssued = null;
        List<DateMods> dateIssueds = originInfo.getDateIssuedList();
        if (dateIssueds != null) {
            Optional<DateMods> date = dateIssueds.stream()
                .filter(predicate)
                .findFirst();
            if (date.isPresent() ) {
                dateIssued = date.get().getValue();
            }
        }
        
        if (isValidDate(dateIssued)) {
            return dateIssued;
        } else {
            return null;
        }
    }
    
    private String getNotEncodedDateValue() {
         return getEncodedDateValue(d -> d != null && d.getPoint() == null);
    }

    private boolean isValidDate(String date) {
        if (date != null) {
             DateValidator validator = DateValidator.getInstance();
             if(validator.isValid(date,"yyyy")
                     || validator.isValid(date,"yyyy-MM")
                     || validator.isValid(date,"yyyy-MM-dd")) {
                 return true;
             }
         }
        return false;
    }

    private String getDateModified() {
        if (originInfo.getDateModified() != null) {
            return originInfo.getDateModified().getValue();
        }
        return null;
    }
    
}
