package no.nb.microservices.catalogitem.rest.controller.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.RelatedItems;
import no.nb.microservices.catalogitem.rest.controller.ItemController;
import no.nb.microservices.catalogitem.rest.model.RelatedItemResource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


public class RelatedItemsResourceAssembler extends ResourceAssemblerSupport<Item, RelatedItemResource> {

    public RelatedItemsResourceAssembler() {
        super(ItemController.class, RelatedItemResource.class);
    }

    @Override
    public RelatedItemResource toResource(Item item) {
        RelatedItemResource resource = new RelatedItemResource();
        resource.add(linkTo(methodOn(ItemController.class).getRelatedItems(item.getId())).withSelfRel());
        ItemResultResourceAssembler assembler = new ItemResultResourceAssembler();
        RelatedItems relatedItems = item.getRelatedItems();
        if (relatedItems != null) {
            resource.setConstituents(assembler.toResources(relatedItems.getConstituents()));
            resource.setHosts(assembler.toResources(relatedItems.getHosts()));
            if (relatedItems.getPreceding() != null) {
                resource.setPreceding(assembler.toResource(relatedItems.getPreceding()));
            }
            if (relatedItems.getSucceding() != null) {
                resource.setSucceding(assembler.toResource(relatedItems.getSucceding()));
            }
        }
        return resource;
    }

}
