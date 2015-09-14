package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by andreasb on 10.09.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Geographic {
    private String placeString;
    private Place place;
    private Coordinates coordinates;

    public Geographic() {
    }

    public String getPlaceString() {
        return placeString;
    }

    public void setPlaceString(String placeString) {
        this.placeString = placeString;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
