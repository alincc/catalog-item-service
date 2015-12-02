package no.nb.microservices.catalogitem.core.utils;

import java.util.List;

public class ItemFields {

    private ItemFields() {
        super();
    }
    
    public static boolean show(List<String> fields, String field) {
        if (fields == null || fields.isEmpty()) {
            return true;
        } else if (fields != null && fields.stream().anyMatch(s -> s.equals("*"))) {
            return true;
        } else if (fields != null && fields.stream().anyMatch(s -> s.startsWith(field))) {
            return true;
        } else {
            return false;
        }
    }
    
}
