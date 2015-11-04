package no.nb.microservices.catalogitem.core.item.service;

import java.util.ArrayList;
import java.util.List;
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
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogitem.core.security.repository.SecurityRepository;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.RelatedItem;
import no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo;
import reactor.Environment;
import reactor.fn.Function;
import reactor.fn.tuple.Tuple3;
import reactor.rx.Stream;
import reactor.rx.Streams;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger LOG = LoggerFactory.getLogger(ItemServiceImpl.class);
    
    final MetadataRepository metadatRepository;
    final SecurityRepository securityRepository;
    final IndexService indexService;

    @Autowired
    public ItemServiceImpl(MetadataRepository metadatRepository, 
            SecurityRepository securityRepository,
            IndexService indexService) {
        super();
        this.metadatRepository = metadatRepository;
        this.securityRepository = securityRepository;
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
            Stream<Item> resp = Streams.zip(getModsById(tracableId), getFieldsById(tracableId), hasAccess(tracableId),
                (Tuple3<Mods, FieldResource, Boolean> tup) -> {
                    Mods mods = tup.getT1();
                    FieldResource fields = tup.getT2();
                    Boolean hasAccess = tup.getT3();

                    RelatedItems relatedItems = null;
                    if ("relatedItems".equalsIgnoreCase(expand)) {
                        List<Item> constituents = getItemByRelatedItemType("constituent", mods, securityInfo);
                        List<Item> hosts = getItemByRelatedItemType("host", mods, securityInfo);
                        List<Item> preceding = getItemByRelatedItemType("preceding", mods, securityInfo);
                        List<Item> succeeding = getItemByRelatedItemType("succeeding", mods, securityInfo);
                        relatedItems = new RelatedItems(constituents, hosts,
                                !preceding.isEmpty() ? preceding.get(0) : null,
                                !succeeding.isEmpty() ? succeeding.get(0) : null);
                    }
                    
                   return new ItemBuilder(id)
                            .mods(mods)
                            .fields(fields)
                            .hasAccess(hasAccess)
                            .withRelatedItems(relatedItems)
                            .build();
                });

            return resp.next().await();
        } catch (Exception ex) {
            LOG.warn("Failed getting item for id " + id, ex);
        }
        return new ItemBuilder(id).build();
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

    private Stream<Mods> getModsById(TracableId id) {
        Environment env = new Environment();
        return Streams.just(id).dispatchOn(env)
                .map(new Function<TracableId, Mods>() {
                    @Override
                    public Mods apply(TracableId id) {
                        Trace.continueSpan(id.getSpan());
                        SecurityInfo securityInfo = id.getSecurityInfo();
                        return metadatRepository.getModsById(id.getId(), securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
                    }
                });
    }

    private Stream<FieldResource> getFieldsById(TracableId id) {
        Environment env = new Environment();
        return Streams.just(id).dispatchOn(env)
                .map(new Function<TracableId, FieldResource>() {
                    @Override
                    public FieldResource apply(TracableId id) {
                        Trace.continueSpan(id.getSpan());
                        SecurityInfo securityInfo = id.getSecurityInfo();
                        return metadatRepository.getFieldsById(id.getId(), securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
                    }
                });
    }

    private Stream<Boolean> hasAccess(TracableId id) {
        Environment env = new Environment();
        return Streams.just(id).dispatchOn(env)
                .map(new Function<TracableId, Boolean>() {
                    @Override
                    public Boolean apply(TracableId id) {
                        Trace.continueSpan(id.getSpan());
                        SecurityInfo securityInfo = id.getSecurityInfo();
                        return securityRepository.hasAccess(id.getId(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
                    }
                });
    }
    
    private List<Item> getItemByRelatedItemType(String type, Mods mods, SecurityInfo securityInfo) {
        List<Item> items = new ArrayList<>();
        List<RelatedItem> relatedItem = mods.getRelatedItems()
            .stream()
            .filter(r -> type.equalsIgnoreCase(r.getType()))
            .collect(Collectors.toList());
        
        relatedItem.forEach(r -> {
            if (r.getRecordInfo() != null && r.getRecordInfo().getRecordIdentifier() != null) {
                String q = "oaiid:\"oai:"+r.getRecordInfo().getRecordIdentifier().getSource()+":" + r.getRecordInfo().getRecordIdentifier().getValue() + "\"";
                SearchResult searchResult = indexService.search(q, securityInfo);
                String id = searchResult.getIds().get(0);
                Item item = getItemById(id, null, securityInfo);
                addPartNumber(r, item);
                items.add(item);
            }
        });
        return items;
    }

    private void addPartNumber(RelatedItem r, Item item) {
        if (item != null) {
            for(TitleInfo titleInfo : item.getMods().getTitleInfos()) {
                titleInfo.setPartNumber(r.getTitleInfo().get(0).getPartNumber());
            }
        }
    }

}

