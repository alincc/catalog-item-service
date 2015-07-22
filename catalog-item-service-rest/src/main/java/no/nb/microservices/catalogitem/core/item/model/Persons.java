package no.nb.microservices.catalogitem.core.item.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;

public class Persons {
    private List<Person> personList = new ArrayList<>();

    private Persons(List<Person> persons) {
        super();
        this.personList = persons;
    }

    public List<Person> getPersons() {
        return Collections.unmodifiableList(personList);
    }
    
    public static class Builder {
        
        private Mods mods;
        
        public Builder(final Mods mods) {
            this.mods = mods != null ? mods : new Mods();
        }
        
        public List<Person> buildList() {
            return new Persons(getPersonalNames(mods.getNames())).getPersons();
        }

        private List<Person> getPersonalNames(List<Name> names) {
            if (names != null) {
                return names.stream()
                    .filter(name -> "personal".equalsIgnoreCase(name.getType()))
                    .map(name -> new Person.PersonBuilder(name).createPerson())
                    .collect(Collectors.toList());
            }
            return new ArrayList<>();
        }        

    }


}
