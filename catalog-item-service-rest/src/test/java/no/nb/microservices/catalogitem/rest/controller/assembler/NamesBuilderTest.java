package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import no.nb.microservices.catalogitem.rest.model.Corporates;
import no.nb.microservices.catalogitem.rest.model.Person;
import no.nb.microservices.catalogitem.rest.model.Role;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Namepart;
import no.nb.microservices.catalogmetadata.model.mods.v3.RoleTerm;
import no.nb.microservices.catalogmetadata.test.mods.v3.NamepartBuilder;
import no.nb.microservices.catalogmetadata.test.mods.v3.RoleTermBuilder;

public class NamesBuilderTest {

    @Test
    public void testPersonals() {
        Name person = createPerson();
        Name corporate = createCorporate();

        List<Name> names = Arrays.asList(person, corporate);
        
        List<Person> persons = new NamesBuilder()
                .withNames(names)
                .buildPersonList();
        
        Person expected = new Person();
        expected.setName("Ola");
        expected.setRoles(Arrays.asList(new Role("cre")));
        expected.setDate("1960-");

        assertEquals("It should be one person", 1, persons.size());
        assertEquals(expected, persons.get(0));
    }

    @Test
    public void testCorporates() {
        Name person = createPerson();
        Name corporate = createCorporate();

        List<Name> names = Arrays.asList(person, corporate);
        
        List<Corporates> persons = new NamesBuilder()
                .withNames(names)
                .buildCorporatesList();
        
        Corporates expected = new Corporates();
        expected.setName("a-ha");
        expected.setRoles(Arrays.asList(new Role("Gruppe - musikk")));

        assertEquals("It should be one person", 1, persons.size());
        assertEquals(expected, persons.get(0));
    }
    
    
    private Name createPerson() {
        Namepart name = new NamepartBuilder()
                .withValue("Ola")
                .build();
        Namepart birthAndDeath = new NamepartBuilder()
                .withType("date")
                .withValue("1960-")
                .build();
        RoleTerm creator = new RoleTermBuilder()
                .withAuthority("marcrelator")
                .withType("code")
                .withValue("cre")
                .build();
        Name person = new no.nb.microservices.catalogmetadata.test.mods.v3.NameBuilder()
            .withType("personal")
            .withNameParts(name, birthAndDeath)
            .withRoleTerms(creator)
            .build();
        return person;
    }

    private Name createCorporate() {
        Namepart name = new NamepartBuilder()
                .withValue("a-ha")
                .build();
        RoleTerm group = new RoleTermBuilder()
                .withValue("Gruppe - musikk")
                .build();
        Name person = new no.nb.microservices.catalogmetadata.test.mods.v3.NameBuilder()
            .withType("corporate")
            .withNameParts(name)
            .withRoleTerms(group)
            .build();
        return person;
    }
    
}
