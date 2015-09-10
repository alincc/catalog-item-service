package no.nb.microservices.catalogitem.core.item.model;

import no.nb.microservices.catalogmetadata.model.mods.v3.Abstract;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andreasb on 10.09.15.
 */
public class Summary {
    private String value;

    private Summary(Builder builder) {
        this.value = builder.getValue();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static class Builder {

        private List<no.nb.microservices.catalogmetadata.model.mods.v3.Abstract> abstracts;

        public Builder(final Mods mods) {
            if (mods != null && mods.getAbstracts() != null) {
                this.abstracts = mods.getAbstracts();
            } else {
                this.abstracts = new ArrayList<>();
            }

        }

        public Summary build() {
            return new Summary(this);
        }

        private String getValue() {
            return (!abstracts.isEmpty()) ? abstracts.get(0).getValue() : "";
        }

    }
}
