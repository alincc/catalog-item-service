package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.routines.DateValidator;

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
            if (date.isPresent()) {
                dateIssued = date.get().getValue();
            }
        }
        return dateIssued;
    }
    
    private String getNotEncodedDateValue() {
         String date = getEncodedDateValue(d -> d != null && d.getPoint() == null);
         if (date != null) {
             DateValidator validator = DateValidator.getInstance();
             if(validator.isValid(date,"yyyy")
                     || validator.isValid(date,"yyyy-MM")
                     || validator.isValid(date,"yyyy-MM-dd")) {
                 return date;
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
