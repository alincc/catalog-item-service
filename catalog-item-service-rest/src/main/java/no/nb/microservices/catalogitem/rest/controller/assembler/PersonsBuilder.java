package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogitem.rest.model.Person;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;

public class PersonsBuilder {

    private List<Name> names;
    
    public PersonsBuilder(final List<Name> names) {
        this.names = names;
    }
    
    public List<Person> buildList() {
        return getPersonalNames();
    }

    private List<Person> getPersonalNames() {
        if (names != null) {
            return names.stream()
                .filter(name -> "personal".equalsIgnoreCase(name.getType()))
                .map(name -> new PersonBuilder(name).createPerson())
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }        
    
}
