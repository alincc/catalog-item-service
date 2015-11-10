package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Namepart;
import no.nb.microservices.catalogmetadata.model.mods.v3.RoleTerm;
import no.nb.microservices.catalogmetadata.model.mods.v3.Subject;
import no.nb.microservices.catalogmetadata.model.mods.v3.Topic;
import no.nb.microservices.catalogmetadata.test.mods.v3.NamepartBuilder;
import no.nb.microservices.catalogmetadata.test.mods.v3.RoleTermBuilder;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubjectBuilderTest {

    @Test
    public void testTopics() {
        Mods mods = new Mods();
        mods.setSubjects(createSubjects());

        no.nb.microservices.catalogitem.rest.model.Subject build = new SubjectBuilder().withSubjects(mods.getSubjects()).build();

        assertEquals(3, build.getTopics().size());
        assertEquals("Ski", getTopicFromResultIgnoreCase(build, "ski"));
        assertEquals("War", getTopicFromResultIgnoreCase(build, "war"));
        assertEquals("Europe", getTopicFromResultIgnoreCase(build, "Europe"));
    }

    @Test
    public void whenNoSubjectsItShouldReturnNull() {
        assertNull("subjects should be null", new SubjectBuilder().withSubjects(null).build());
    }
    
    @Test
    public void testPersonal() {
        Mods mods = new Mods();
        mods.setSubjects(createSubjects());

        no.nb.microservices.catalogitem.rest.model.Subject subject = new SubjectBuilder().withSubjects(mods.getSubjects()).build();

        assertEquals(1, subject.getPersons().size());
        
    }

    private List<Subject> createSubjects() {
        Subject subject1 = new Subject();
        subject1.setTopic(createTopics().subList(0, 2));
        Subject subject2 = new Subject();
        subject2.setTopic(createTopics().subList(2, 3));
        
        Subject subject3 = new Subject();
        subject3.setNames(Arrays.asList(createPerson()));
        
        return Arrays.asList(subject1, subject2, subject3);
    }

    private List<Topic> createTopics() {
        Topic topic1 = new Topic();
        topic1.setValue("Ski");
        Topic topic2 = new Topic();
        topic2.setValue("War");
        Topic topic3 = new Topic();
        topic3.setValue("Europe");
        return Arrays.asList(topic1, topic2, topic3);
    }
    
    private String getTopicFromResultIgnoreCase(
            no.nb.microservices.catalogitem.rest.model.Subject build,
            String topic) {
        return build.getTopics().stream().filter(q -> q.equalsIgnoreCase(topic)).findFirst().get();
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
    
}
