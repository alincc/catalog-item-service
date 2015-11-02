package no.nb.microservices.catalogitem.rest.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TitleInfo {
    private String title;
    private String type;
    private String partNumber;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(title)
            .append(type)
            .append(partNumber)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof TitleInfo){
            final TitleInfo other = (TitleInfo) obj;
            return new EqualsBuilder()
                .append(title, other.title)
                .append(type, other.type)
                .append(partNumber, other.partNumber)
                .isEquals();
        } else{
            return false;
        }
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).
          append("title", title).
          append("type", type).
          append("partNumber", partNumber).
          toString();
      }    
    
}

