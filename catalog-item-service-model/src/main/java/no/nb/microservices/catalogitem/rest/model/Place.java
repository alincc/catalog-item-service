package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by andreasb on 11.09.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Place {
    private String country;
    private String region;
    private String city;
    private String area;

    public Place() {
    }

    public Place(String country, String region, String city, String area) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.area = area;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
