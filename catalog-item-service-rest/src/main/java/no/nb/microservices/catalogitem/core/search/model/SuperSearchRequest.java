package no.nb.microservices.catalogitem.core.search.model;

import java.util.List;
import java.util.stream.Collectors;

public class SuperSearchRequest {
    private final SearchRequest searchRequest;
    private final List<String> possibleMediaTypesToSearch;
    private List<String> wantedMediaTypesToSearch;
    private List<String> otherMediaTypesToSearch;

    public SuperSearchRequest(SearchRequest searchRequest, List<String> possibleMediaTypesToSearch) {
        this.searchRequest = searchRequest;
        this.possibleMediaTypesToSearch = possibleMediaTypesToSearch;
        initWantedMediaTypesToSearch();
        initOtherMediaTypes();
    }

    private void initWantedMediaTypesToSearch() {
        if(searchRequest.getMediatypes().isEmpty()) {
            wantedMediaTypesToSearch = possibleMediaTypesToSearch;
        } else {
            wantedMediaTypesToSearch = searchRequest.getMediatypes().stream().map(String::toLowerCase).collect(Collectors.toList());
        }
    }

    private void initOtherMediaTypes() {
         otherMediaTypesToSearch = possibleMediaTypesToSearch
                .stream()
                .filter(mediaType -> !wantedMediaTypesToSearch.contains(mediaType))
                .map(mediaType -> "mediatype:" + mediaType)
                .collect(Collectors.toList());
    }

    public List<String> getMediaTypesToSearchFor() {
        return wantedMediaTypesToSearch;
    }

    public List<String> getOtherMediaTypes() {
        return otherMediaTypesToSearch;
    }

    public SearchRequest getSearchRequest() {
        return searchRequest;
    }
}
