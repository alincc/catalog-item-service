package no.nb.microservices.catalogitem.core.security.service;

import java.util.concurrent.Future;

import no.nb.microservices.catalogitem.core.item.service.TracableId;

public interface SecurityService {
    Future<Boolean> hasAccess(TracableId id);
}