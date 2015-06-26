package no.nb.microservices.catalogitem.core.item.service;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogitem.core.security.repository.SecurityRepository;
import no.nb.microservices.catalogitem.utils.ModsOriginInfoExtractor;
import no.nb.microservices.catalogitem.utils.ModsPersonExtractor;
import no.nb.microservices.catalogmetadata.model.fields.Fields;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ronnymikalsen
 *
 */
@Service
public class ItemServiceImpl implements IItemService {

    final MetadataRepository metadatRepository;
    final SecurityRepository securityRepository;
    
    @Autowired
    public ItemServiceImpl(MetadataRepository metadatRepository, SecurityRepository securityRepository) {
        super();
        this.metadatRepository = metadatRepository;
        this.securityRepository = securityRepository;
    }

    @Override
    public Item getItemById(String id) {
        Mods mods = metadatRepository.getModsById(id);
        Fields fields = metadatRepository.getFieldsById(id);
        
        Item item = new Item();
        item.setId(id);
        item.setTitle(mods.getTitleInfos().iterator().next().getTitle());
        item.setOrigin(ModsOriginInfoExtractor.extractOriginInfo(mods));
        
        populateAccessInfo(item, fields);
        populatePeople(item, mods);
        return item;
    }

    private void populateAccessInfo(Item item, Fields fields) {
        item.getAccessInfo().setDigital(fields.isDigital());
        item.getAccessInfo().setContentClasses(fields.getContentClasses());
        item.getAccessInfo().setHasAccess(securityRepository.hasAccess(item.getId()));
    }

    private void populatePeople(Item item, Mods mods) {
        item.setPersons(ModsPersonExtractor.extractPersons(mods));
    }

}
