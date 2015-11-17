package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.RecordInfo;

public class RecordInfoBuilder {

    private no.nb.microservices.catalogmetadata.model.mods.v3.RecordInfo recordInfo;

    public RecordInfoBuilder withRecordInfo(final no.nb.microservices.catalogmetadata.model.mods.v3.RecordInfo recordInfo) {
        this.recordInfo = recordInfo;
        return this;
    }

    public RecordInfo build() {
        if (recordInfo == null) {
            return null;
        }
        
        RecordInfo recordInfo = new RecordInfo(getIdentifier(), getIdentifierSource(), getCreated());
        
        return recordInfo.isEmpty() ? null : recordInfo;
    }

    private String getIdentifier() {
        if (recordInfo.getRecordIdentifier() != null) {
            return recordInfo.getRecordIdentifier().getValue();
        }
        return null;
    }

    private String getIdentifierSource() {
        if (recordInfo.getRecordIdentifier() != null) {
            return recordInfo.getRecordIdentifier().getSource();
        }
        return null;
    }

    private String getCreated() {
        if (recordInfo.getCreationDate() != null) {
            return recordInfo.getCreationDate().toString();
        }
        return null;
    }
}