package no.nb.microservices.catalogitem.core.search.model;

import no.nb.microservices.catalogsearchindex.NBSearchType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String q;
    private List<String> fields;
    private List<String> sort = new ArrayList<>();
    private List<String> boost = new ArrayList<>();
    private String aggs;
    private NBSearchType searchType;
    private String topRight;
    private String bottomLeft;
    private String precision;

    public SearchRequest() {
        super();
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getSort() {
        removeEncoding();
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = Arrays.asList(sort);
    }

    public List<String> getBoost() {
        return boost;
    }

    public void setBoost(String[] boost) {
        this.boost = Arrays.asList(boost);
    }
    public String getAggs() {
        return aggs;
    }

    public void setAggs(String aggs) {
        this.aggs = aggs;
    }

    public NBSearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(NBSearchType searchType) {
        this.searchType = searchType;
    }

    public String getTopRight() {
        return topRight;
    }

    public void setTopRight(String topRight) {
        this.topRight = topRight;
    }

    public String getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(String bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    private void removeEncoding() {
        if (sort != null) {
            for (int i = 0; i < sort.size(); i++) {
                sort.set(i, sort.get(i).replace("%2C",","));
            }
        }
    }


}
