package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import org.apache.commons.validator.routines.DateValidator;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OriginInfoBuilder {

    private no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo originInfo;
    private no.nb.microservices.catalogsearchindex.ItemResource itemResource;
    private boolean simplified = true;
    
    public OriginInfoBuilder withOriginInfo(final no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo originInfo) {
        this.originInfo = originInfo;
        return this;
    }

    public OriginInfoBuilder withItemResource(no.nb.microservices.catalogsearchindex.ItemResource itemResource) {
        this.itemResource = itemResource;
        return this;
    }

    public OriginInfoBuilder withExpand() {
        this.simplified = false;
        return this;
    }

    public OriginInfo build() {
        if (this.simplified) {
            return buildSimplified();
        }

        if (originInfo == null) {
            return null;
        }
        return buildFull();
    }

    public OriginInfo buildSimplified() {
        OriginInfo originInfoSimple = new OriginInfo();
        originInfoSimple.setIssued(getDateIssued());
        return originInfoSimple.isEmpty() ? null : originInfoSimple;
    }

    public OriginInfo buildFull() {
        OriginInfo originInfoFull = new OriginInfo(getPublisher(), getDateIssued(), getFrequency(),
                getDateCreated(), getDateCaptured(), getDateModified(), getEdition(), getFirstIndexTime());
        return originInfoFull.isEmpty() ? null : originInfoFull;
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

    private String getFirstIndexTime() {
        if (itemResource != null && itemResource.getFirstIndexTime() != null) {
            return itemResource.getFirstIndexTime();
        }
        return null;
    }

    private String getDateIssued() {
        String dateIssued = null;
        if (itemResource == null) {
            dateIssued = getEncodedDateValue(d -> "w3cdtf".equals(d.getEncoding()));
            if (dateIssued == null) {
                dateIssued = getEncodedDateValue(d -> "marc".equals(d.getEncoding()));
            }
            if (dateIssued == null) {
                dateIssued = getNotEncodedDateValue();
            }
        } else {
            if (isValidDate(itemResource.getDateIssued())) {
                dateIssued = itemResource.getDateIssued();
            }
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
}