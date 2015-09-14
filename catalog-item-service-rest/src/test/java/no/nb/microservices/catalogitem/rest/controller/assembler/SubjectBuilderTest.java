package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Subject;
import no.nb.microservices.catalogmetadata.model.mods.v3.Topic;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SubjectBuilderTest {

    @Test
    public void subjectTopicsTest() {
        List<Subject> subjects = getSubjects();
        Mods mods = new Mods();
        mods.setSubjects(subjects);

        SubjectBuilder builder = new SubjectBuilder(mods);
        no.nb.microservices.catalogitem.rest.model.Subject build = builder.build();

        assertEquals(3, build.getTopics().size());
        assertEquals("Ski", getTopicFromResultIgnoreCase(build, "ski"));
        assertEquals("War", getTopicFromResultIgnoreCase(build, "war"));
        assertEquals("Europe", getTopicFromResultIgnoreCase(build, "Europe"));
    }

    private String getTopicFromResultIgnoreCase(
            no.nb.microservices.catalogitem.rest.model.Subject build,
            String topic) {
        return build.getTopics().stream().filter(q -> q.equalsIgnoreCase(topic)).findFirst().get();
    }

    @Test
    public void subjectTopicsEmptyTest() {
        Mods mods = new Mods();
        mods.setSubjects(null);

        SubjectBuilder builder = new SubjectBuilder(mods);
        no.nb.microservices.catalogitem.rest.model.Subject build = builder.build();

        assertEquals(0, build.getTopics().size());
    }

    private List<Subject> getSubjects() {
        Subject subject1 = new Subject();
        subject1.setTopic(getTopics().subList(0, 2));
        Subject subject2 = new Subject();
        subject2.setTopic(getTopics().subList(2, 3));
        return Arrays.asList(subject1, subject2);
    }

    private List<Topic> getTopics() {
        Topic topic1 = new Topic();
        topic1.setValue("Ski");
        Topic topic2 = new Topic();
        topic2.setValue("War");
        Topic topic3 = new Topic();
        topic3.setValue("Europe");

        return Arrays.asList(topic1, topic2, topic3);

    }
}
