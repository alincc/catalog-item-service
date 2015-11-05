package no.nb.microservices.catalogitem.core.item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.htrace.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.Item.ItemBuilder;
import no.nb.microservices.catalogitem.core.item.model.RelatedItems;
import no.nb.microservices.catalogitem.core.metadata.service.MetadataService;
import no.nb.microservices.catalogitem.core.security.service.SecurityService;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.RelatedItem;
import no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger LOG = LoggerFactory.getLogger(ItemServiceImpl.class);
    
    final MetadataService metadataService;
    final SecurityService securityService;
    final IndexService indexService;

    @Autowired
    public ItemServiceImpl(MetadataService metadataService, 
            SecurityService securityService,
            IndexService indexService) {
        super();
        this.metadataService = metadataService;
        this.securityService = securityService;
        this.indexService = indexService;
    }

    @Override
    public Item getItemById(String id, String expand) {
        SecurityInfo securityInfo = getSecurityInfo();
        return getItemById(id, expand, securityInfo);
    }
    
    public Item getItemById(String id, String expand, SecurityInfo securityInfo) {

        try {
            TracableId tracableId = new TracableId(Trace.currentSpan(), id, securityInfo);
            Future<Mods> mods = metadataService.getModsById(tracableId);
            Future<FieldResource> fields = metadataService.getFieldsById(tracableId);
            Future<Boolean> hasAccess = securityService.hasAccess(tracableId);
            
            while (!(mods.isDone() && fields.isDone() && hasAccess.isDone())) {
                Thread.sleep(1);
            }
            RelatedItems relatedItems = getRelatedItems(expand, securityInfo, mods.get());
            return new ItemBuilder(id)
                    .mods(mods.get())
                    .fields(fields.get())
                    .hasAccess(hasAccess.get())
                    .withRelatedItems(relatedItems)
                    .build();
        } catch (Exception ex) {
            LOG.warn("Failed getting item for id " + id, ex);
        }
        return new ItemBuilder(id).build();
    }

    private RelatedItems getRelatedItems(String expand,
            SecurityInfo securityInfo, Mods mods) {
        RelatedItems relatedItems = null;
        if ("relatedItems".equalsIgnoreCase(expand)) {
            List<Item> constituents = getItemByRelatedItemType("constituent", mods, securityInfo);
            List<Item> hosts = getItemByRelatedItemType("host", mods, securityInfo);
            List<Item> preceding = getItemByRelatedItemType("preceding", mods, securityInfo);
            List<Item> succeeding = getItemByRelatedItemType("succeeding", mods, securityInfo);
            Item series = getSeries(mods, securityInfo);
            relatedItems = new RelatedItems(constituents, hosts,
                    !preceding.isEmpty() ? preceding.get(0) : null,
                    !succeeding.isEmpty() ? succeeding.get(0) : null,
                    series);
        }
        return relatedItems;
    }

    private Item getSeries(Mods mods, SecurityInfo securityInfo) {
        List<Item> series = getItemByRelatedItemType("series", mods, securityInfo);
        return !series.isEmpty() ? series.get(0) : null;
    }

    private SecurityInfo getSecurityInfo() {
        SecurityInfo securityInfo = new SecurityInfo();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        securityInfo.setxHost(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_HOST));
        securityInfo.setxPort(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_PORT));
        securityInfo.setxRealIp(UserUtils.getClientIp(request));
        securityInfo.setSsoToken(UserUtils.getSsoToken(request));
        return securityInfo;
    }

    private List<Item> getItemByRelatedItemType(String type, Mods mods, SecurityInfo securityInfo) {
        List<Item> items = new ArrayList<>();
        List<RelatedItem> relatedItem = mods.getRelatedItems()
            .stream()
            .filter(r -> type.equalsIgnoreCase(r.getType()))
            .collect(Collectors.toList());
        
        relatedItem.forEach(r -> {
            String query = getQueryFromRecordIdentifier(mods, r); 
            if (query == null) {
                query = getQueryFromIdentifier(mods, r);
            }
             
            if (query != null) {
                SearchResult searchResult = indexService.search(query, securityInfo);
                if (!searchResult.getIds().isEmpty()) {
                    String id = searchResult.getIds().get(0);
                    Item item = getItemById(id, null, securityInfo);
                    addPartNumber(r, item);
                    items.add(item);
                }
            }
        });
        return items;
    }

    private String getQueryFromIdentifier(Mods mods, RelatedItem r) {
        String query = null;
        if (r.getIdentifier() != null) {
            if ("oaiid".equals(r.getIdentifier().getType())) {
                query = "oaiid:\""+r.getIdentifier().getValue()  + "\"";
            } else if ("local".equals(r.getIdentifier().getType())) {
                query = "oaiid:\"oai:"+mods.getRecordInfo().getRecordIdentifier().getSource()+":" + r.getIdentifier().getValue() + "\"";
            }
        }
        return query;
    }

    private String getQueryFromRecordIdentifier(Mods mods, RelatedItem r) {
        String query = null;
        if (r.getRecordInfo() != null && r.getRecordInfo().getRecordIdentifier() != null) {
            String source = null;
            String identifier = null;
            source = r.getRecordInfo().getRecordIdentifier().getSource(); 
            if (source == null && mods.getRecordInfo() != null && mods.getRecordInfo().getRecordIdentifier() != null) {
                source = mods.getRecordInfo().getRecordIdentifier().getSource();
            }
            identifier = r.getRecordInfo().getRecordIdentifier().getValue();
            query = "oaiid:\"oai:"+source+":" + identifier + "\"";
        }
        return query;
    }

    private void addPartNumber(RelatedItem r, Item item) {
        if (item != null) {
            for(TitleInfo titleInfo : item.getMods().getTitleInfos()) {
                titleInfo.setPartNumber(r.getTitleInfo().get(0).getPartNumber());
            }
        }
    }

}

