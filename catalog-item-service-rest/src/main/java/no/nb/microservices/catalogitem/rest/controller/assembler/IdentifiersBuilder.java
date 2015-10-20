package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<String> isbn10 = getIsbn10(getMultipleIdentifierByType("isbn"));
        identifiers.setIsbn10(isbn10);
        List<String> isbn13 = getIsbn13(getMultipleIdentifierByType("isbn"));
        identifiers.setIsbn13(isbn13);
        List<String> issn = getMultipleIdentifierByType("issn");
        identifiers.setIssn(issn);
        return identifiers;
    }

    private List<String> getMultipleIdentifierByType(String type) {
        List<String> identifierByType = new ArrayList<>();
        if (mods != null && mods.getIdentifiers() != null) {
            for(Identifier identifier : mods.getIdentifiers()) {
                if (type.equalsIgnoreCase(identifier.getType())){
                    identifierByType.add(identifier.getValue());
                }
            }
        }
        return identifierByType;
    }

    private List<String> getIsbn10(List<String> tmpList){
        List<String> idByType = getIsbnList(tmpList, 10);
        return idByType;
    }

    private List<String> getIsbn13(List<String> tmpList){
        List<String> idByType = getIsbnList(tmpList, 13);
        return idByType;
    }

    private List<String> getIsbnList(List<String> tmpList, int type){
        List<String> idByType = new ArrayList<>();
        for (int i = 0; i < tmpList.size(); i++){
            if (ifValidIsbn(tmpList.get(i), type)){
                idByType.add(tmpList.get(i).substring(0,type));
            }
        }
        return idByType;
    }

    private boolean ifValidIsbn(String isbn, int isbnType){
        boolean valid = false;
        int counter = 0;
        for (int i = 0; i < isbn.length(); i++){
            if (Character.isDigit(isbn.charAt(i))){
                counter++;
            }
        }
        if (counter == isbnType){
            valid = true;
        }
        return valid;
    }

    private List<String> getUrns() {
        if (field != null) {
            return field.getUrns();
        }
        return null;
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

}
