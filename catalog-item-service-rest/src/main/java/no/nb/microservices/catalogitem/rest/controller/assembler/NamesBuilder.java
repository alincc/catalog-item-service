package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Corporates;
import no.nb.microservices.catalogitem.rest.model.Person;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NamesBuilder {

    private List<Name> names;
    
    public NamesBuilder(final List<Name> names) {
        this.names = names;
    }
    
    public List<Person> buildPersonList() {
        return getPersonalNames();
    }

    public List<Corporates> buildCorporatesList() {
        return getCorporateName();
    }

    private List<Person> getPersonalNames() {
        if (names != null) {
            return names.stream()
                .filter(name -> "personal".equalsIgnoreCase(name.getType()))
                .map(name -> new NameBuilder(name).createPerson())
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<Corporates> getCorporateName() {
        if (names != null) {
            return names.stream()
                    .filter(name -> "corporate".equalsIgnoreCase(name.getType()))
                    .map(name -> new NameBuilder(name).createCorporate())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}