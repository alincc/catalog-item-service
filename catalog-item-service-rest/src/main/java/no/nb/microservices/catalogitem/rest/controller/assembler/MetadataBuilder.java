package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Classification;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogitem.rest.model.OriginInfo;
import no.nb.microservices.catalogitem.rest.model.Person;
import no.nb.microservices.catalogitem.rest.model.Role;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;

public final class MetadataBuilder {

    private final Item item;
    
    public MetadataBuilder(Item item) {
        super();
        this.item = item;
    }

    public Metadata build() {
        Metadata metadata = new Metadata();
        metadata.setCompositeTitle(item.getTitle());
        
        TitleInfoDirector titleInfoDirector = new TitleInfoDirector();
        
        TitleInfo standardTitleInfo = titleInfoDirector.createTitleInfo(new StandardTitleInfoBuilder(), item);
        metadata.setTitleInfo(standardTitleInfo);
        
        TitleInfo alternativeTitleInfo = titleInfoDirector.createTitleInfo(new AlternativeTitleInfoBuilder(), item);
        metadata.setAlternativeTitleInfo(alternativeTitleInfo);
        
        createPeople(item, metadata);
        createOriginInfo(item, metadata);
        createClassification(item, metadata);
        return metadata;
    }
    
    private void createOriginInfo(Item item, Metadata metadata) {
        if (item.getOrigin() == null) {
            return;
        }
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPublisher(item.getOrigin().getPublisher());
        originInfo.setCaptured(item.getOrigin().getDateCaptured());
        originInfo.setCreated(item.getOrigin().getDateCreated());
        originInfo.setEdition(item.getOrigin().getEdition());
        originInfo.setFrequency(item.getOrigin().getFrequency());
        originInfo.setIssued(item.getOrigin().getDateIssued());
        originInfo.setModified(item.getOrigin().getDateModified());
        
        metadata.setOriginInfo(originInfo);
    }
    
    private void createPeople(Item item, Metadata metadata) {
        if (item.getPersons() == null || item.getPersons().isEmpty()) {
            return;
        }
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < item.getPersons().size(); i++) {
            Person person = new Person();
            person.setName(item.getPersons().get(i).getName());
            person.setDate(item.getPersons().get(i).getBirthAndDeathYear());
            List<Role> roles = new ArrayList<>();
            for (String roleName : item.getPersons().get(i).getRoles()) {
                Role role = new Role();
                role.setName(roleName);
                roles.add(role);
            }
            person.setRoles(roles);
            people.add(person);
        }
        metadata.setPeople(people);
    }

    private void createClassification(Item item, Metadata metadata) {
       Classification classification = new Classification();
       
       createDdc(item, classification);
       createUdc(item, classification);
       
       metadata.setClassification(classification);
        
    }
    
    private void createDdc(Item item, Classification classification) {
        Iterator<String> iter = item.getClassification().getDdc().iterator();
           while (iter.hasNext()) {
               classification.addDdc(iter.next());
           }
    }

    private void createUdc(Item item, Classification classification) {
        Iterator<String> iter = item.getClassification().getUdc().iterator();
           while (iter.hasNext()) {
               classification.addUdc(iter.next());
           }
    }
    
}
