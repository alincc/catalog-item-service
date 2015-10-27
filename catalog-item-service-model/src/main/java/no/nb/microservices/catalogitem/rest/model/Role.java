package no.nb.microservices.catalogitem.rest.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    private String name;

    public Role() {
        super();
    }
    
    public Role(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(name)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Role){
            final Role other = (Role) obj;
            return new EqualsBuilder()
                .append(name, other.name)
                .isEquals();
        } else{
            return false;
        }
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).
          append("name", name).
          toString();
      }    
    
}
