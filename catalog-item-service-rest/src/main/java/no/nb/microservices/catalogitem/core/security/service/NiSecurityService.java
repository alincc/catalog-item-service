package no.nb.microservices.catalogitem.core.security.service;

import java.util.concurrent.Future;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.core.security.repository.SecurityRepository;

@Service
public class NiSecurityService implements SecurityService {

    @Autowired
    SecurityRepository securityRepository;
    
    @Override
    @Async
    public Future<Boolean> hasAccess(TracableId id) {
        Trace.continueSpan(id.getSpan());
        SecurityInfo securityInfo = id.getSecurityInfo();
        Boolean hasAccess =  securityRepository.hasAccess(id.getId(), securityInfo.getxRealIp(), securityInfo.getSsoToken());
        return new AsyncResult<Boolean>(hasAccess);
    }

}
