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
    private List<String> mediaTypes = new ArrayList<>();

    public SuperSearchRequest() {
    }

    public List<String> getWantedMediaTypes(List<String> possibleMediaTypesToSearch) {
        if (mediaTypes.isEmpty()) {
            return possibleMediaTypesToSearch;
        } else {
            return mediaTypes.stream().filter(possibleMediaTypesToSearch::contains).map(String::toLowerCase).collect(Collectors.toList());
        }
    }

    public List<String> getOtherMediaTypes(List<String> possibleMediaTypesToSearch, List<String> wantedMediaTypesToSearch) {
         return possibleMediaTypesToSearch
                .stream()
                .filter(mediaType -> !wantedMediaTypesToSearch.contains(mediaType))
                .map(mediaType -> "mediatype:" + mediaType)
                .collect(Collectors.toList());
    }

    public List<String> getMediaTypes() {
        super.removeEncoding(mediaTypes);
        return mediaTypes;
    }

    public void setMediaTypes(String[] mediaTypes) {
        this.mediaTypes = Arrays.asList(mediaTypes);
    }
}
