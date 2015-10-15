package no.nb.microservices.catalogitem.rest.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamingInfo {
    private String identifier;
    private Integer offset;
    private Integer extent;

    public StreamingInfo(String identifier, Integer offset, Integer extent) {
        super();
        this.identifier = identifier;
        this.offset = offset;
        this.extent = extent;
    }

    public String getIdentifier() {
        return identifier;
    }
    
    public Integer getOffset() {
        return offset;
    }

    public Integer getExtent() {
        return extent;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(identifier)
            .append(offset)
            .append(extent)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof StreamingInfo){
            final StreamingInfo other = (StreamingInfo) obj;
            return new EqualsBuilder()
                .append(identifier, other.identifier)
                .append(offset, other.offset)
                .append(extent, other.extent)
                .isEquals();
        } else{
            return false;
        }
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).
          append("identifier", identifier).
          append("offset", offset).
          append("extent", extent).
          toString();
      }    
}
