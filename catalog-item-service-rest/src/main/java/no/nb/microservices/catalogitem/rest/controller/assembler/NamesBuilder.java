package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Corporates;
import no.nb.microservices.catalogitem.rest.model.Person;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NamesBuilder {
    private static final String PERSONAL = "personal";
    private static final String CORPORATE = "corporate";
    private List<Name> names;

    public NamesBuilder withNames(final List<Name> names) {
        this.names = names;
        return this;
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
                    .filter(name -> PERSONAL.equalsIgnoreCase(name.getType()))
                    .map(name -> new NameBuilder(name).createPerson())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<Corporates> getCorporateName() {
        List<Corporates> corporates = new ArrayList<>();
        if (names != null) {
            for (Name name : names) {
                if (CORPORATE.equalsIgnoreCase(name.getType())) {
                    corporates.addAll(new NameBuilder(name).createCorporate());
                }
            }
        }
        return corporates;
    }

}