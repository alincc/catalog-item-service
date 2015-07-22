package no.nb.microservices.catalogitem.core.item.model;

import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;

public class Origin {
    private String publisher;
    private String dateIssued;
    private String dateCreated;
    private String dateCaptured;
    private String dateModified;
    private String frequency;
    private String edition;

    private Origin(Builder builder) {
        this.dateIssued = builder.getDateIssued();
        this.dateCreated = builder.getDateCreated();
        this.dateCaptured = builder.getDateCaptured();
        this.dateModified = builder.getDateModified();
        this.publisher = builder.getPublisher();
        this.frequency = builder.getFrequency();
        this.edition = builder.getEdition();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCaptured() {
        return dateCaptured;
    }

    public void setDateCaptured(String dateCaptured) {
        this.dateCaptured = dateCaptured;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }
    
    public static class Builder {
        
        private OriginInfo originInfo;
        
        public Builder mods(final Mods mods) {
            if (mods == null) {
                this.originInfo = new OriginInfo();
            } else {
                this.originInfo = mods.getOriginInfo() != null ? mods.getOriginInfo() : new OriginInfo();
            }
            return this;
        }
        
        public Origin build() {
            return new Origin(this);
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
     
        private String getDateCaptured() {
            if (originInfo.getDateCaptured() != null) {
                return originInfo.getDateCaptured().getValue();
            }
            return null;
        }
        
        private String getDateModified() {
            if (originInfo.getDateModified() != null) {
                return originInfo.getDateModified().getValue();
            }
            return null;
        }

        private String getPublisher() {
            if (originInfo.getPublisher() != null) {
                return originInfo.getPublisher();
            }
            return null;
        }

        private String getFrequency() {
            if (originInfo.getFrequency() != null) {
                return originInfo.getFrequency();
            }
            return null;
        }

        private String getEdition() {
            if (originInfo.getEdition() != null) {
                return originInfo.getEdition();
            }
            return null;
        }

    }
}
