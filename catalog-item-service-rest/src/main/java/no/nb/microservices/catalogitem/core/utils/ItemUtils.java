package no.nb.microservices.catalogitem.core.utils;

import java.util.List;

public class ItemUtils {

    private ItemUtils() {
        super();
    }
    
    public static boolean showField(List<String> fields, String field) {
        if ((fields == null || fields.isEmpty()) ||
            (fields != null && fields.stream().anyMatch(s -> "*".equals(s))) ||
            (fields != null && fields.stream().anyMatch(s -> s.startsWith(field)))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isExpand(String expand, String field) {
        if (expand == null || expand.isEmpty()) {
            return false;
        }
        boolean isExpand = false;
        String[] split = expand.split(",");
        for (String s : split) {
            if (s.equalsIgnoreCase(field)) {
                isExpand = true;
                break;
            }
        }
        return isExpand;
    }
    
}
