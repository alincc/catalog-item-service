package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.mods.v3.Identifier;
import no.nb.microservices.catalogsearchindex.ItemResource;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class IdentifiersBuilder {
    private ItemResource itemResource;
    private List<Identifier> identifiers;
    private boolean simplified = true;

    public IdentifiersBuilder withIdentifiers(final List<Identifier> identifiers) {
        this.identifiers = identifiers;
        return this;
    }

    public IdentifiersBuilder withItemResource(final ItemResource itemResource) {
        this.itemResource = itemResource;
        return this;
    }

    public IdentifiersBuilder withExpand() {
        this.simplified = false;
        return this;
    }

    public Identifiers build() {
        if (this.simplified) {
            return buildSimplified();
        }
        if (identifiers == null) {
            return null;
        }
        return buildFull();
    }

    public Identifiers buildSimplified() {
        Identifiers ids = new Identifiers();
        ids.setUrn(getUrn());

        return ids.isEmpty() ? null : ids;
    }

    public Identifiers buildFull() {
        Identifiers ids = new Identifiers(getIsbn10(), getIsbn13(), getMultipleIdentifierByType("issn"),
                getIdentifierByType("sesamid"), getIdentifierByType("oaiid"), getIdentifierByType("urn"));

        return ids.isEmpty() ? null : ids;
    }

    private List<String> getIsbn10() {
        List<String> isbns = getMultipleIdentifierByType("isbn");
        return filterIsbnByLength(isbns, 10);
    }

    private List<String> getIsbn13() {
        List<String> isbns = getMultipleIdentifierByType("isbn");
        return filterIsbnByLength(isbns, 13);
    }

    private String getIdentifierByType(String type) {
        String identifierByType = null;
        for (Identifier identifier : identifiers) {
            if (type.equalsIgnoreCase(identifier.getType())) {
                identifierByType = identifier.getValue();
            }
        }
        return identifierByType;
    }

    private String getUrn() {
        if (itemResource != null) {
            return itemResource.getUrn();
        }
        return null;
    }

    private List<String> getMultipleIdentifierByType(String type) {
        List<String> identifierByType = new ArrayList<>();
        identifierByType = identifiers.stream()
                .filter(identifier -> type.equalsIgnoreCase(identifier.getType()))
                .map(identifier -> identifier.getValue())
                .collect(Collectors.toList());
        return identifierByType;
    }

    private List<String> filterIsbnByLength(List<String> isbns, int length) {
        List<String> idByType = new ArrayList<>();
        for (String isbn : isbns) {
            isbn = removeNonDigits(isbn);
            if (validateIsbn(isbn, length)) {
                idByType.add(isbn);
            }
        }
        return idByType;
    }

    private String removeNonDigits(String isbn) {
        return isbn.replaceAll("[^\\d]", "");
    }

    private boolean validateIsbn(String isbn, int length) {
        return isbn.length() == length && NumberUtils.isDigits(isbn);
    }

}
