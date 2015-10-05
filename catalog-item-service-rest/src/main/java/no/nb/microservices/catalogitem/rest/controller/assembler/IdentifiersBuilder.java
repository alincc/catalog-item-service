package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;


public class IdentifiersBuilder {
    private final FieldResource field;

    public IdentifiersBuilder(FieldResource field) {
        this.field = field;
    }

    public Identifiers build() {
        Identifiers identifiers = new Identifiers();
        identifiers.setUrns(field.getUrns());
        identifiers.setSesamId(field.getSesamId());

        return identifiers;
    }
}
