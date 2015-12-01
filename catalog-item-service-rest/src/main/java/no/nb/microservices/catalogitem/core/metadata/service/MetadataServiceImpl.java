package no.nb.microservices.catalogitem.core.metadata.service;

import java.util.concurrent.Future;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

@Service
public class MetadataServiceImpl  implements MetadataService{

    MetadataRepository metadataRepository;
    
    @Autowired
    public MetadataServiceImpl(MetadataRepository metadataRepository) {
        super();
        this.metadataRepository = metadataRepository;
    }

    @Override
    @Async
    public Future<Mods> getModsById(TracableId id) {
      Trace.continueSpan(id.getSpan());
      SecurityInfo securityInfo = id.getSecurityInfo();
      Mods mods = metadataRepository.getModsById(id.getId(), securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
      return new AsyncResult<Mods>(mods);
    }

}
