package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Corporates;
import no.nb.microservices.catalogitem.rest.model.Person;
import no.nb.microservices.catalogitem.rest.model.Role;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Namepart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NameBuilder {

    private Name name;

    public NameBuilder(final Name name) {
        this.name = name;
    }

    public Person createPerson() {
        Person person = new Person();
        person.setName(getName());
        person.setDate(getDateOfBirthAndDeath());
        person.setRoles(getRoles());
        return person;
    }

    public List<Corporates> createCorporate() {
        List<Corporates> corporates = new ArrayList<>();
        for (Namepart namepart : name.getNameParts()) {
            if (namepart.getValue() != null) {
                corporates.add(new Corporates(namepart.getValue(), getDateOfBirthAndDeath(), getRoles()));
            }
        }
        return corporates;
    }

    private String getName() {
        for (Namepart namepart : name.getNameParts()) {
            if (namepart.getType() == null) {
                return namepart.getValue();
            }
        }
        return null;
    }

    private String getDateOfBirthAndDeath() {
        if (name.getNameParts() != null) {
            for (Namepart namepart : name.getNameParts()) {
                if ("date".equalsIgnoreCase(namepart.getType())) {
                    return namepart.getValue();
                }
            }
        }
        return null;
    }

    private List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        for (String roleTerm : getRoleTerms()) {
            roles.add(new Role(roleTerm));
        }
        return roles;
    }

    private List<String> getRoleTerms() {
        List<String> roleTerms = new ArrayList<>();
        if (name.getRole() != null) {
            for (no.nb.microservices.catalogmetadata.model.mods.v3.Role role : name.getRole()) {
                if (role.getRoleTerms() == null) {
                    continue;
                }
                roleTerms.addAll(role.getRoleTerms().stream()
                        .map(r -> r.getValue())
                        .collect(Collectors.toList()));
            }
        }
        return roleTerms;
    }
}