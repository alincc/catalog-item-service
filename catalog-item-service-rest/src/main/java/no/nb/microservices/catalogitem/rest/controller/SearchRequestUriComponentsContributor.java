package no.nb.microservices.catalogitem.rest.controller;

import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogsearchindex.NBSearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.hateoas.mvc.UriComponentsContributor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SearchRequestUriComponentsContributor implements UriComponentsContributor {

    private static final Logger LOG = LoggerFactory.getLogger(SearchRequestUriComponentsContributor.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == SearchRequest.class;
    }

    @Override
    public void enhance(UriComponentsBuilder builder, MethodParameter parameter, Object value) {
        if(value instanceof SearchRequest) {
            SearchRequest searchRequest = (SearchRequest)value;
            Field[] declaredFields = SearchRequest.class.getDeclaredFields();
            for(Field field : declaredFields) {
                Method[] methods = SearchRequest.class.getMethods();
                for (Method method : methods) {
                    if(isGetMethodForField(field.getName(), method)) {
                        populateUriWithQueryParam(builder, field.getName(), getValueFromMethod(searchRequest, method));
                    }
                }
            }
        }
    }

    private boolean isGetMethodForField(String fieldName, Method method) {
        return method.getName().equalsIgnoreCase("get" + fieldName) || method.getName().equalsIgnoreCase("is" + fieldName);
    }

    private Object getValueFromMethod(SearchRequest searchRequest, Method method) {
        if(searchRequest != null) {
            try {
                return method.invoke(searchRequest);
            } catch (IllegalAccessException e) {
                LOG.debug("Cant access method {}", method.getName(), e);
                LOG.error("Cant access method {}", method.getName());
            } catch (InvocationTargetException e) {
                LOG.debug("Cant invoke method {}", method.getName(), e);
                LOG.error("Cant invoke method {}", method.getName());
            }
        }
        return null;
    }

    private UriComponentsBuilder populateUriWithQueryParam(UriComponentsBuilder builder, String fieldName, Object fieldValue) {
        if(fieldValue != null) {
            if (fieldValue instanceof List) {
                List list = (List) fieldValue;
                if(!list.isEmpty()) {
                    if ("mediatypes".equalsIgnoreCase(fieldName)) {
                        builder.queryParam(fieldName, String.join(",", list));
                    } else {
                        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                        params.put(fieldName, list);
                        builder.queryParams(params);
                    }
                }
            } else if (shouldAddQueryParam(fieldValue)) {
                builder.queryParam(fieldName, fieldValue);
            }
        }

        return builder;
    }

    private boolean shouldAddQueryParam(Object fieldValue) {
        if (fieldValue instanceof String) {
            return !((String) fieldValue).isEmpty();
        } else if (fieldValue instanceof NBSearchType) {
            return true;
        } else if (fieldValue instanceof Boolean) {
            return (boolean) fieldValue;
        }
        return false;
    }
}
