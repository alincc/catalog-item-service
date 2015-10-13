package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.List;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Identifier;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;


public class IdentifiersBuilder {
    private FieldResource field;
    private Mods mods;

    public IdentifiersBuilder withField(final FieldResource field) {
        this.field = field;
        return this;
    }

    public IdentifiersBuilder withMods(final Mods mods) {
        this.mods = mods;
        return this;
    }

    public Identifiers build() {
        Identifiers identifiers = new Identifiers();
        identifiers.setUrns(getUrns());
        String sesamid = getIdentifierByType("sesamid");
        identifiers.setSesamId(sesamid);
        String oaiid = getIdentifierByType("oaiid");
        identifiers.setOaiId(oaiid);
        return identifiers;
    }

    private List<String> getUrns() {
        if (field != null) {
            return field.getUrns();
        }
        return null;
    }

    private String getIdentifierByType(String type) {
        String idByType = null;
        if (mods != null && mods.getIdentifiers() != null) {
            for(Identifier identifier : mods.getIdentifiers()) {
                if (type.equalsIgnoreCase(identifier.getType())) {
                    idByType = identifier.getValue();
                }
            }
        }
        return idByType;
    }

}
