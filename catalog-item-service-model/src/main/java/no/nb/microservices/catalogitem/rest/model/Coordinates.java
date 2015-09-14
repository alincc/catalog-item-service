package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by andreasb on 11.09.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
