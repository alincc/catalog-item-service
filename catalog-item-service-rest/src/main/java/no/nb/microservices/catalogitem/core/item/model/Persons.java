package no.nb.microservices.catalogitem.core.item.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;

public class Persons {
    private List<Person> persons = new ArrayList<>();

    private Persons(List<Person> persons) {
        super();
        this.persons = persons;
    }

    public List<Person> getPersons() {
        return Collections.unmodifiableList(persons);
    }
    
    public static class PersonsBuilder {
        
        private Mods mods;
        
        public PersonsBuilder(final Mods mods) {
            this.mods = mods != null ? mods : new Mods();
        }
        
        public List<Person> buildList() {
            Iterator<Name> iter = getPersonalNames(mods.getNames()).iterator();
            List<Person> persons = new ArrayList<>();
            while (iter.hasNext()) {
                persons.add(new Person.PersonBuilder(iter.next()).createPerson());
            }
            return new Persons(persons).getPersons();
        }

        private List<Name> getPersonalNames(List<Name> names) {
            if (names == null) {
                names = new ArrayList<>();
            }
            
            return names.stream()
                    .filter(name -> "personal".equalsIgnoreCase(name.getType()))
                    .collect(Collectors.toList());
        }        

    }


}
