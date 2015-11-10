package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Person;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectBuilder {

    private List<Subject> subjects;

    public SubjectBuilder withSubjects(final List<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }

    public no.nb.microservices.catalogitem.rest.model.Subject build() {
        if (!getTopics().isEmpty() || !getPersons().isEmpty()) {
            return new no.nb.microservices.catalogitem.rest.model.Subject(getTopics(),getPersons());
        }
        return null;
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

    private List<Person> getPersons() {
        List<Person> persons = new ArrayList<>();
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject subject : subjects) {
            	List<Name> names = subject.getNames();
            	if (names != null)  {
            		for(Name name : names) {
            			if ("personal".equals(name.getType())) {
            				persons.add(new NameBuilder(name).createPerson());			
            			}
            		}
            	}
            }
        }

        return persons;
    }

}
