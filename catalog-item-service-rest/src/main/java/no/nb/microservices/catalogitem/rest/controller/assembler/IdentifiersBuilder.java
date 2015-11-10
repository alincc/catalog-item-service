package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.mods.v3.Identifier;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class IdentifiersBuilder {
    private List<Identifier> identifiers;

    public IdentifiersBuilder withIdentifiers(final List<Identifier> identifiers) {
        this.identifiers = identifiers;
        return this;
    }

    public Identifiers build() {
        if (identifiers == null) {
            return null;
        }
        if (!getIsbn10().isEmpty() || !getIsbn13().isEmpty() || getIdentifierByType("sesamid") != null || getIdentifierByType("oaiid") != null
                || getIdentifierByType("issn") != null || getIdentifierByType("urn") != null) {
            return new Identifiers(getIsbn10(),getIsbn13(),getMultipleIdentifierByType("issn"),
                    getIdentifierByType("sesamid"), getIdentifierByType("oaiid"), getIdentifierByType("urn"));
        }
        return null;
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
