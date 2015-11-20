package no.nb.microservices.catalogitem.core.search.model;

import no.nb.microservices.catalogsearchindex.NBSearchType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String q;
    private String fields;
    private List<String> sort = new ArrayList<>();
    private String aggs;
    private NBSearchType searchType;

    public SearchRequest() {

    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public List<String> getSort() {
        removeEncoding();
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = Arrays.asList(sort);
    }

    public String getAggs() {
        return aggs;
    }

    public void setAggs(String aggs) {
        this.aggs = aggs;
    }

    private void removeEncoding() {
        if (sort != null) {
            for (int i = 0; i < sort.size(); i++) {
                sort.set(i, sort.get(i).replace("%2C",","));
            }
        }
    }

    public NBSearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(NBSearchType searchType) {
        this.searchType = searchType;
    }
}
