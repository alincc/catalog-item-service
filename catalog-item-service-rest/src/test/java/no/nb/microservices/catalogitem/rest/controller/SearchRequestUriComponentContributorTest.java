package no.nb.microservices.catalogitem.rest.controller;

import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RunWith(MockitoJUnitRunner.class)
public class SearchRequestUriComponentContributorTest {

    private SearchRequestUriComponentsContributor searchRequestUriComponentsContributor = new SearchRequestUriComponentsContributor();

    @Before
    public void init() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/search?q=*");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void whenSearchRequestIsParameterThenSupportParameter() throws Exception {
        MethodParameter m = new MethodParameter(SearchController.class.getMethod("search", SearchRequest.class, Pageable.class), 0);
        assertThat(searchRequestUriComponentsContributor.supportsParameter(m), is(true));
    }

    @Test
    public void whenSearchRequestIsNotParameterThenDontSupportParameter() throws Exception {
        MethodParameter m = new MethodParameter(SearchController.class.getMethod("search", String.class), 0);
        assertThat(searchRequestUriComponentsContributor.supportsParameter(m), is(false));
    }

    @Test
    public void whenSearchingWithOneShouldThenGetOnlyOneShouldBeInUri() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("*");
        searchRequest.setShould(new String[]{"title,peter"});
        UriComponentsBuilder builder = getUriComponentsBuilder(searchRequest);

        searchRequestUriComponentsContributor.enhance(builder, null, searchRequest);

        assertThat(builder.toUriString(), is("http://localhost/catalog/v1/search?q=*&should=title,peter"));
    }

    @Test
    public void whenSearchingWithTwoShouldThenGetTwoShouldBeInUri() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("*");
        searchRequest.setShould(new String[]{"title,peter", "title,pan"});
        UriComponentsBuilder builder = getUriComponentsBuilder(searchRequest);

        searchRequestUriComponentsContributor.enhance(builder, null, searchRequest);

        assertThat(builder.toUriString(), is("http://localhost/catalog/v1/search?q=*&should=title,peter&should=title,pan"));
    }

    @Test
    public void whenSearchingWithTwoMediaTypesThenOneMediaTypeParameterWithTwoValuesShouldBeInUri() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("*");
        searchRequest.setMediatypes(new String[]{"aviser", "radio"});
        UriComponentsBuilder builder = getUriComponentsBuilder(searchRequest);

        searchRequestUriComponentsContributor.enhance(builder, null, searchRequest);

        assertThat(builder.toUriString(), is("http://localhost/catalog/v1/search?q=*&mediatypes=aviser,radio"));
    }

    private UriComponentsBuilder getUriComponentsBuilder(SearchRequest searchRequest) {
        return linkTo(methodOn(SearchController.class).search(searchRequest, new PageRequest(0, 10))).toUriComponentsBuilder();
    }
}
