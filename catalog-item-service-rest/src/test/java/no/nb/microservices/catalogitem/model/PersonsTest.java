package no.nb.microservices.catalogitem.utils;

import no.nb.microservices.catalogitem.core.item.model.Person;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Namepart;
import no.nb.microservices.catalogmetadata.model.mods.v3.Role;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ModsPersonExtractorTest {

    @Test
    public void shouldReturnListOfNames() {
        Mods mods = new Mods();
        Name n1 = new Name();
        n1.setType("personal");

        Namepart np1 = new Namepart();
        np1.setValue("Myhre, Margareta Magnus");

        Namepart np2 = new Namepart();
        np2.setType("date");
        np2.setValue("1970-");

        Role r1 = new Role();
        r1.setRoleTerms(Arrays.asList("red."));
        n1.setRole(Arrays.asList(r1));
        n1.setNameParts(Arrays.asList(np1,np2));

        Name n2 = new Name();
        Namepart np3 = new Namepart();
        np3.setValue("Kurt Josef");
        n2.setNameParts(Arrays.asList(np3));
        mods.setNames(Arrays.asList(n1,n2));

        List<Person> persons = ModsPersonExtractor.extractPersons(mods);

        assertEquals("Should return list with 1 person",1,persons.size());
        assertEquals("Name should be Myhre, Margareta Magnus", "Myhre, Margareta Magnus", persons.get(0).getName());
        assertEquals("Date should be 1970-", "1970-", persons.get(0).getDate());
    }

    @Test
    public void shouldReturnEmptyListWhenModsHaveNoNames() {
        Mods mods = new Mods();
        List<Person> persons = ModsPersonExtractor.extractPersons(mods);
        assertEquals("List should be empty", 0, persons.size());
    }
}
