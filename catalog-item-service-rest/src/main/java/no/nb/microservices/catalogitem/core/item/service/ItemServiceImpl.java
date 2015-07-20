package no.nb.microservices.catalogitem.core.item.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogitem.core.security.repository.SecurityRepository;
import no.nb.microservices.catalogitem.utils.ModsOriginInfoExtractor;
import no.nb.microservices.catalogitem.utils.ModsPersonExtractor;
import no.nb.microservices.catalogmetadata.model.fields.Fields;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import reactor.Environment;
import reactor.fn.Function;
import reactor.fn.tuple.Tuple2;
import reactor.fn.tuple.Tuple3;
import reactor.rx.Stream;
import reactor.rx.Streams;

/**
 * 
 * @author ronnymikalsen
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

    final MetadataRepository metadatRepository;
    final SecurityRepository securityRepository;

    @Autowired
    public ItemServiceImpl(MetadataRepository metadatRepository, 
            SecurityRepository securityRepository) {
        super();
        this.metadatRepository = metadatRepository;
        this.securityRepository = securityRepository;
    }

    @Override
    public Item getItemById(String id) {

        try {
            SecurityInfo securityInfo = getSecurityInfo();

            TracableId tracableId = new TracableId(Trace.currentSpan(), id, securityInfo);
            Stream<Item> resp = Streams.zip(getModsById(tracableId), getFieldsById(tracableId), hasAccess(tracableId),
                (Tuple3<Mods, Fields, Boolean> tup) -> {
                    Mods mods = tup.getT1();
                    Fields fields = tup.getT2();
                    Boolean hasAccess = tup.getT3();
                    
                    Item item = new Item();
                    item.setId(id);
                    item.setTitle(mods.getTitleInfos().iterator().next().getTitle());
                    item.setOrigin(ModsOriginInfoExtractor.extractOriginInfo(mods));
                    
                    populateAccessInfo(item, fields, hasAccess);
                    populatePeople(item, mods);
                    return item;
                    
                });

            return resp.next().await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

        
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

    public Stream<Mods> getModsById(TracableId id) {
        Environment env = new Environment();
        return Streams.just(id).dispatchOn(env)
                .map(new Function<TracableId, Mods>() {
                    @Override
                    public Mods apply(TracableId id) {
                        Trace.continueSpan(id.getSpan());
                        SecurityInfo securityInfo = id.getSecurityInfo();
                        System.out.println(id.getId());
                        return metadatRepository.getModsById(id.getId(), securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
                    }
                }).onErrorReturn(Exception.class, new Function<Exception, Mods>() {
                    @Override
                    public Mods apply(Exception t) {
                        System.out.println(">>>>>>>>>>>>>>>>>> " + t.getMessage() + " <<<<<<<<<<<<<<<<<<<<");
                        System.out.println(t);
                        return new Mods();
                    }
                });
    }

    public Stream<Fields> getFieldsById(TracableId id) {
        Environment env = new Environment();
        return Streams.just(id).dispatchOn(env)
                .map(new Function<TracableId, Fields>() {
                    @Override
                    public Fields apply(TracableId id) {
                        Trace.continueSpan(id.getSpan());
                        SecurityInfo securityInfo = id.getSecurityInfo();
                        return metadatRepository.getFieldsById(id.getId(), securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
                    }
                }).onErrorReturn(Exception.class, new Function<Exception, Fields>() {
                    @Override
                    public Fields apply(Exception t) {
                        System.out.println(">>>>>>>>>>>>>>>>>> " + t.getMessage() + " <<<<<<<<<<<<<<<<<<<<");
                        System.out.println(t);
                        return new Fields();
                    }
                });
    }

    public Stream<Boolean> hasAccess(TracableId id) {
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
    
    
    private void populateAccessInfo(Item item, Fields fields, Boolean hasAccess) {
        item.getAccessInfo().setDigital(fields.isDigital());
        item.getAccessInfo().setContentClasses(fields.getContentClasses());
        item.getAccessInfo().setHasAccess(hasAccess);
    }

    private void populatePeople(Item item, Mods mods) {
        item.setPersons(ModsPersonExtractor.extractPersons(mods));
    }

}
