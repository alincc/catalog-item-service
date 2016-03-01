package no.nb.microservices.catalogitem.core.metadata.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class MetadataServiceImpl  implements MetadataService{

    MetadataRepository metadataRepository;

    @Autowired
    public MetadataServiceImpl(MetadataRepository metadataRepository) {
        super();
        this.metadataRepository = metadataRepository;
    }

    @Async
    @Override
    @HystrixCommand(fallbackMethod = "getModsFallback")
    public Future<Mods> getModsById(TracableId id) {
        Trace.continueSpan(id.getSpan());
        SecurityInfo securityInfo = id.getSecurityInfo();
        return new AsyncResult<Mods>() {
            @Override
            public Mods invoke() {
                return metadataRepository.getModsById(id.getId(), securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
            }
        };
    }

    public Mods getModsFallback(TracableId id) {
        Trace.continueSpan(id.getSpan());
        return new Mods();
    }

}
