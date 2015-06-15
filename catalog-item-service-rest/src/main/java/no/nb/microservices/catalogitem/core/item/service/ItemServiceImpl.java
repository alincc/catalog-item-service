package no.nb.microservices.catalogitem.core.item.service;

import no.nb.microservices.catalogitem.core.item.model.IItemService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
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
    
    @Autowired
    public ItemServiceImpl(MetadataRepository metadatRepository) {
        super();
        this.metadatRepository = metadatRepository;
    }

    @Override
    public Item getItemById(String id) {
        Mods mods = metadatRepository.getModsById(id);
        Fields fields = metadatRepository.getFieldsById(id);
        
        Item item = new Item();
        item.setId(id);
        item.setTitle(mods.getTitleInfos().iterator().next().getTitle());
        
        populateAccessInfo(fields, item);
        
        return item;
    }

    private void populateAccessInfo(Fields fields, Item item) {
        item.getAccessInfo().setDigital(fields.isDigital());
    }

}
