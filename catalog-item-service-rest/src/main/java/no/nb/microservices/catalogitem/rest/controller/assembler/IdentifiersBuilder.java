package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.mods.v3.Identifier;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;


public class IdentifiersBuilder {
    private Mods mods;

    public IdentifiersBuilder withMods(final Mods mods) {
        this.mods = mods;
        return this;
    }

    public Identifiers build() {
        Identifiers identifiers = new Identifiers();
        identifiers.setIsbn10(getIsbn10());
        identifiers.setIsbn13(getIsbn13());
        identifiers.setSesamId(getIdentifierByType("sesamid"));
        identifiers.setOaiId(getIdentifierByType("oaiid"));
        identifiers.setIssn(getMultipleIdentifierByType("issn"));
        identifiers.setUrn(getIdentifierByType("urn"));
        return identifiers;
    }

    private List<String> getIsbn10(){
        List<String> isbns = getMultipleIdentifierByType("isbn");
        return filterIsbnByLength(isbns, 10);
    }
    
    private List<String> getIsbn13(){
        List<String> isbns = getMultipleIdentifierByType("isbn");
        return filterIsbnByLength(isbns, 13);
    }
    
    private String getIdentifierByType(String type) {
        String identifierByType = null;
        if (mods != null && mods.getIdentifiers() != null) {
            for(Identifier identifier : mods.getIdentifiers()) {
                if (type.equalsIgnoreCase(identifier.getType())) {
                    identifierByType = identifier.getValue();
                }
            }
        }
        return identifierByType;
    }
    
    private List<String> getMultipleIdentifierByType(String type) {
        List<String> identifierByType = new ArrayList<>();
        if (mods != null && mods.getIdentifiers() != null) {
            identifierByType = mods.getIdentifiers().stream()
                    .filter(identifier -> type.equalsIgnoreCase(identifier.getType()))
                    .map(identifier -> identifier.getValue())
                    .collect(Collectors.toList());
        }
        return identifierByType;
    }

    private List<String> filterIsbnByLength(List<String> isbns, int length){
        List<String> idByType = new ArrayList<>();
        for (String isbn : isbns){
            isbn = removeNonDigits(isbn);
            if (validateIsbn(isbn, length)){
                idByType.add(isbn);
            }
        }
        return idByType;
    }

    private String removeNonDigits(String isbn) {
        return isbn.replaceAll("[^\\d]", "");
    }

    private boolean validateIsbn(String isbn, int length){
        return isbn.length() == length && NumberUtils.isDigits(isbn);
    }

}
