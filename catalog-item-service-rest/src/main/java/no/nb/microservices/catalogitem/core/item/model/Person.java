package no.nb.microservices.catalogitem.core.item.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Namepart;
import no.nb.microservices.catalogmetadata.model.mods.v3.Role;

public final class Person {
    private String name;
    private String birthAndDeathYear;
    private List<String> roles;

    private Person(String name, String birthAndDeathYear, List<String> roles) {
        super();
        this.birthAndDeathYear = birthAndDeathYear;
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public String getBirthAndDeathYear() {
        return birthAndDeathYear;
    }

    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }


    public static class PersonBuilder {
        
        private Name name;
        
        public PersonBuilder(final Name name) {
            this.name = name;
        }
        
        public Person createPerson() {
            return new Person(getName(), getDateOfBirthAndDeath(), getRoles());
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
        
        private List<String> getRoles() {
            List<String> roles = new ArrayList<>();
            if (name.getRole() != null) {
                for (Role role : name.getRole()) {
                    if (role.getRoleTerms() == null) {
                        continue;
                    }
                    roles.addAll(role.getRoleTerms().stream().collect(Collectors.toList()));
                }
            }
            return roles;
        }
        

    }
    
}
