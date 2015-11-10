package no.nb.microservices.catalogitem.rest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Classification {
    private List<String> ddc = new ArrayList<>();
    private List<String> udc = new ArrayList<>();

    public Classification(List<String> ddc, List<String> udc) {
        this.ddc = ddc;
        this.udc = udc;
    }

    public void addDdc(String ddc) {
        this.ddc.add(ddc);
    }
    
    public List<String> getDdc() {
        return Collections.unmodifiableList(ddc);
    }

    public void addUdc(String udc) {
        this.udc.add(udc);
    }

    public List<String> getUdc() {
        return Collections.unmodifiableList(udc);
    }

}
