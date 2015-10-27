package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Corporates {
    private String name;
    private String date;
    private List<Role> roles;

    @JsonCreator
    public Corporates() {
        super();
    }
    
    public Corporates(String name, String date, List<Role> roles) {
        this.name = name;
        this.date = date;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(name)
            .append(date)
            .append(roles)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Corporates){
            final Corporates other = (Corporates) obj;
            return new EqualsBuilder()
                .append(name, other.name)
                .append(date, other.date)
                .append(roles, other.roles)
                .isEquals();
        } else{
            return false;
        }
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).
          append("name", name).
          append("date", date).
          append("roles", roles).
          toString();
      }    
    
}