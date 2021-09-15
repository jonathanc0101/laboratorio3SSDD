package bordero.client;

import bordero.dto.DTO;
import bordero.dto.ResponseDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Component
public class RestCRUDClient<T extends DTO> {

    private final RestTemplate rest;
    private Class<T> clazz;
    private String url;
    private String elemURL;
    private String fieldURL;

    public RestCRUDClient() {
//        ClientHttpRequestFactory factory =
//                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
//        this.rest = new RestTemplate(factory);
//        List<ClientHttpRequestInterceptor> interceptors = rest.getInterceptors();
//        if (CollectionUtils.isEmpty(interceptors)) {
//            interceptors = new ArrayList<>();
//        }
//        interceptors.add(new bordero.client.LoggingInterceptor());
//        rest.setInterceptors(interceptors);
        this.rest = new RestTemplate();
        rest.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void setUrl(String url) {
        this.url = url;
        this.elemURL = url + "/{id}";
    }

    public void setFieldURL(String url) {
        this.fieldURL = url;
    }

    public ResponseEntity<ResponseDTO<T>> create(T t) {
        return rest.exchange(url, HttpMethod.POST, entity(t), response(clazz));
    }

    public ResponseEntity<ResponseDTO<T>> findById(long id) {
        return rest.exchange(elemURL, HttpMethod.GET, entity(), response(clazz), id);
    }

    public ResponseEntity<ResponseDTO<T>> findOne(Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return rest.exchange(builder.toUriString(), HttpMethod.GET, entity(), response(this.clazz));
    }


    public ResponseEntity<List<T>> getall() {
        return rest.exchange(url, HttpMethod.GET, entity(), list(this.clazz));
    }

    public ResponseEntity<Page<T>> getPage(PageRequest pagReq) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("size",pagReq.getPageSize());
        builder.queryParam("page", pagReq.getPageNumber());
        pagReq.getSort().stream().forEachOrdered( sort -> {
            builder.queryParam("sort", sort.getProperty() + "," + sort.getDirection().name());
        });
        String uri = builder.toUriString();
        return rest.exchange(uri, HttpMethod.GET, entity(), page(this.clazz));
    }

    public ResponseEntity<List<T>> query(Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return rest.exchange(builder.toUriString(), HttpMethod.GET, entity(), list(this.clazz));
    }

    public ResponseEntity<ResponseDTO<T>> update(T c) {
        return rest.exchange(url, HttpMethod.PUT, entity(c), response(clazz));
    }

    public ResponseEntity<ResponseDTO<T>> delete(long id) {
        return rest.exchange(elemURL, HttpMethod.DELETE, entity(), response(clazz), id);
    }

    public ResponseEntity<ResponseDTO<T>> findByField(String field) {
        return rest.exchange(fieldURL, HttpMethod.GET, entity(), response(clazz), field);
    }

    public ResponseEntity<List<T>> findByField(int field) {
        Map<String, Integer> vars = new HashMap<>();
        vars.put("field", field);
        return rest.exchange(fieldURL, HttpMethod.GET, entity(), list(this.clazz), field);
    }

    public ResponseEntity<ResponseDTO<T>> findByCodigo(int codigo) {
        return rest.exchange(fieldURL, HttpMethod.GET, entity(), response(clazz), codigo);
    }

    private ParameterizedTypeReference<ResponseDTO<T>> response(Class<T> clazz) {
        return ParameterizedTypeReference.forType(
                ResolvableType.forClassWithGenerics(ResponseDTO.class, clazz).getType());
    }

    private ParameterizedTypeReference<List<T>> list(Class<T> clazz) {
        return ParameterizedTypeReference.forType(
                ResolvableType.forClassWithGenerics(List.class, clazz).getType());
    }

    private ParameterizedTypeReference<Page<T>> page(Class<T> clazz) {
        return ParameterizedTypeReference.forType(
                ResolvableType.forClassWithGenerics(RestPageImpl.class, clazz).getType());
    }

    private HttpEntity<T> entity() {
        return new HttpEntity<>(json());
    }

    private HttpEntity<T> entity(T t) {
        return new HttpEntity<>(t, json());
    }

    private HttpHeaders json() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
