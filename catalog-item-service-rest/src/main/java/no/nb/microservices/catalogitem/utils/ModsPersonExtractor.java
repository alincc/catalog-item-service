package no.nb.microservices.catalogitem.utils;

import no.nb.microservices.catalogitem.core.item.model.Origin;
import no.nb.microservices.catalogitem.core.item.model.Person;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Namepart;
import no.nb.microservices.catalogmetadata.model.mods.v3.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModsPersonExtractor {

    private ModsPersonExtractor() {
    }

    public static List<Person> extractPersons(Mods mods) {
        List<Person> persons = new ArrayList<>();
        if (mods.getNames() == null) {
            return persons;
        }
        List<Name> names = extractPersonalNames(mods.getNames());
        for (Name name : names) {
            persons.add(extractPerson(name));
        }
        return persons;
    }

    private static List<Name> extractPersonalNames(List<Name> names) {
        return names.stream()
                .filter(name -> "personal".equalsIgnoreCase(name.getType()))
                .collect(Collectors.toList());
    }

    private static Person extractPerson(Name name) {
        Person person = new Person();
        for (Namepart namepart : name.getNameParts()) {
            if ("date".equalsIgnoreCase(namepart.getType())) {
                person.setDate(namepart.getValue());
            } else if (namepart.getType() == null) {
                person.setName(namepart.getValue());
            }
        }
        populateRoles(person, name.getRole());
        return person;
    }

    private static void populateRoles(Person person, List<Role> roles) {
        if (roles == null) {
            return;
        }
        List<String> list = new ArrayList<>();
        for (Role role : roles) {
            if (role.getRoleTerms() == null) {
                continue;
            }
            list.addAll(role.getRoleTerms().stream().collect(Collectors.toList()));
        }
        person.setRoles(list);
    }

}
