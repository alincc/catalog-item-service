package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Subject;

public class SubjectBuilder {

    private List<Subject> subjects;

    public SubjectBuilder(final Mods mods) {
        if (mods == null) {
            this.subjects = Arrays.asList(new Subject());
        } else {
            this.subjects = mods.getSubjects() != null ? mods.getSubjects() : Arrays.asList(new Subject());
        }
    }

    public no.nb.microservices.catalogitem.rest.model.Subject build() {
        no.nb.microservices.catalogitem.rest.model.Subject subject = new no.nb.microservices.catalogitem.rest.model.Subject();
        subject.setTopics(getTopics());

        return subject;
    }

    private List<String> getTopics() {
        List<String> topics = new ArrayList<>();
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject subject : subjects.stream().filter(q -> q.getTopic() != null).collect(Collectors.toList())) {
                topics.addAll(subject.getTopic().stream().map(q -> q.getValue()).collect(Collectors.toList()));
            }
        }

        return topics;
    }

}
