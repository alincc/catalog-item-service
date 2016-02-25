package no.nb.microservices.catalogitem.core.search.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuperSearchRequest extends SearchRequest {
    private List<String> mediatypes = new ArrayList<>();

    public SuperSearchRequest() {
    }

    public List<String> getWantedMediaTypes(List<String> possibleMediaTypesToSearch) {
        if(mediatypes.isEmpty()) {
            return possibleMediaTypesToSearch;
        } else {
            return mediatypes.stream().filter(possibleMediaTypesToSearch::contains).map(String::toLowerCase).collect(Collectors.toList());
        }
    }

    public List<String> getOtherMediaTypes(List<String> possibleMediaTypesToSearch, List<String> wantedMediaTypesToSearch) {
         return possibleMediaTypesToSearch
                .stream()
                .filter(mediaType -> !wantedMediaTypesToSearch.contains(mediaType))
                .map(mediaType -> "mediatype:" + mediaType)
                .collect(Collectors.toList());
    }

    public List<String> getMediatypes() {
        super.removeEncoding(mediatypes);
        return mediatypes;
    }

    public void setMediatypes(String[] mediatypes) {
        this.mediatypes = Arrays.asList(mediatypes);
    }
}
