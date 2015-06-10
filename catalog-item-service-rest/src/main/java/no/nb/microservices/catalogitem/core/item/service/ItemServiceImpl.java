package no.nb.microservices.catalogitem.core.item.service;

import java.util.List;

import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogmetadata.model.fields.Field;
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
        List<Field> fields = metadatRepository.getFieldsById(id);
        
        Item item = new Item();
        item.setId(id);
        item.setTitle(mods.getTitleInfos().iterator().next().getTitle());
        
        populateAccessInfo(fields, item);
        
        return item;
    }

    private void populateAccessInfo(List<Field> fields, Item item) {
        String digital = getNamedField("digital", fields).getValue();
        item.getAccessInfo().setDigital(digital.equals("Ja") ? true : false);
    }

    private Field getNamedField(String name, List<Field> fields) {
        for(Field field : fields) {
            if (field.getName().equalsIgnoreCase(name)) {
                return field;
            }
        }
        return null;
    }

}
