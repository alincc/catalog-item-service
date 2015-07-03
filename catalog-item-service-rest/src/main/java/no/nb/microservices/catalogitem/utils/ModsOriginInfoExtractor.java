package no.nb.microservices.catalogitem.utils;

import no.nb.microservices.catalogitem.core.item.model.Origin;
import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;

public class ModsOriginInfoExtractor {

    private ModsOriginInfoExtractor() {
    }

    public static Origin extractOriginInfo(Mods mods) {
        if (mods.getOriginInfo() == null) {
            return null;
        }
        Origin origin = new Origin();
        OriginInfo originInfo = mods.getOriginInfo();
        populateDateIssued(origin, originInfo);
        populateDateCreated(origin, originInfo);
        populateDateModified(origin, originInfo);
        populateDateCaptured(origin, originInfo);
        populateFrequency(origin, originInfo);
        populateEdition(origin, originInfo);
        populatePublisher(origin, originInfo);

        return origin;
    }

    private static void populateEdition(Origin origin, OriginInfo originInfo) {
        if (originInfo.getEdition() == null) {
            return;
        }
        origin.setEdition(originInfo.getEdition());
    }

    private static void populateFrequency(Origin origin, OriginInfo originInfo) {
        if (originInfo.getFrequency() == null) {
            return;
        }
        origin.setFrequency(originInfo.getFrequency());
    }

    private static void populateDateCaptured(Origin origin, OriginInfo originInfo) {
        if (originInfo.getDateCaptured() == null) {
            return;
        }
        origin.setDateCaptured(originInfo.getDateCaptured().getValue());
    }

    private static void populateDateModified(Origin origin, OriginInfo originInfo) {
        if (originInfo.getDateModified() == null) {
            return;
        }
        origin.setDateModified(originInfo.getDateModified().getValue());
    }

    private static void populateDateCreated(Origin origin, OriginInfo originInfo) {
        if (originInfo.getDateCreated() == null || originInfo.getDateCreated().isEmpty()) {
            return;
        }
        for (DateMods dateMods : originInfo.getDateCreated()) {
            if ("yes".equalsIgnoreCase(dateMods.getKeyDate())) {
                origin.setDateCreated(dateMods.getValue());
                return;
            }
        }
    }

    private static void populateDateIssued(Origin origin, OriginInfo originInfo) {
        if (originInfo.getDateIssuedList() == null || originInfo.getDateIssuedList().isEmpty()) {
            return;
        }
        for (DateMods dateMods : originInfo.getDateIssuedList()) {
            if (dateMods.getEncoding() == null && dateMods.getPoint() == null) {
                origin.setDateIssued(dateMods.getValue());
                return;
            }
        }
    }

    private static void populatePublisher(Origin origin, OriginInfo originInfo) {
        if (originInfo.getPublisher() == null) {
            return;
        }
        origin.setPublisher(originInfo.getPublisher());
    }
}
