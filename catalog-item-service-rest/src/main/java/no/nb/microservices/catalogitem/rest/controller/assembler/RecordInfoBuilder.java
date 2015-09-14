package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.OriginInfo;
import no.nb.microservices.catalogitem.rest.model.RecordInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class RecordInfoBuilder {

    private no.nb.microservices.catalogmetadata.model.mods.v3.RecordInfo recordInfo;

    public RecordInfoBuilder mods(final Mods mods) {
        if (mods == null) {
            this.recordInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.RecordInfo();
        } else {
            this.recordInfo = mods.getRecordInfo() != null ? mods.getRecordInfo() : new no.nb.microservices.catalogmetadata.model.mods.v3.RecordInfo();
        }
        return this;
    }

    public RecordInfo build() {
        RecordInfo recordInfo = new RecordInfo();
        recordInfo.setIdentifier(getIdentifier());
        recordInfo.setIdentifierSource(getIdentifierSource());
        recordInfo.setCreated(getCreated());

        return recordInfo;
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
        return null;
    }

    
}
