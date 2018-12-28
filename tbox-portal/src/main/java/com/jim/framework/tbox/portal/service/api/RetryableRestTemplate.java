package com.jim.framework.tbox.portal.service.api;

import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.common.exception.RestApiException;
import com.jim.framework.tbox.core.RegisterDomainConsts;
import com.jim.framework.tbox.core.dto.ServiceDTO;
import com.jim.framework.tbox.core.enums.Tool;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import javax.annotation.PostConstruct;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

/**
 * 封装RestTemplate. admin server集群在某些机器宕机或者超时的情况下轮询重试
 */
public class RetryableRestTemplate {

    private Logger logger = LoggerFactory.getLogger(RetryableRestTemplate.class);

    private UriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();

    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateFactory restTemplateFactory;
    @Autowired
    private ServiceAddressLocator serviceAddressLocator;


    @PostConstruct
    private void postConstruct() {
        restTemplate = restTemplateFactory.getObject();
    }

    public <T> T get(RestRequest restRequest, String path, Class<T> responseType, Object... urlVariables)
            throws RestClientException {
        return execute(GET, restRequest, path, null, responseType, urlVariables);
    }

    public <T> ResponseEntity<T> get(RestRequest restRequest, String path, ParameterizedTypeReference<T> reference,
                                     Object... uriVariables)
            throws RestClientException {

        return exchangeGet(restRequest, path, reference, uriVariables);
    }

    public <T> T post(RestRequest restRequest, String path, Object request, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return execute(POST, restRequest, path, request, responseType, uriVariables);
    }

    public void put(RestRequest restRequest, String path, Object request, Object... urlVariables) throws RestClientException {
        execute(PUT, restRequest, path, restRequest, null, urlVariables);
    }

    public void delete(RestRequest restRequest, String path, Object... urlVariables) throws RestClientException {
        execute(DELETE, restRequest, path, null, null, urlVariables);
    }

    private <T> T execute(HttpMethod method, RestRequest restRequest, String path, Object request, Class<T> responseType,
                          Object... uriVariables) {
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }

        String uri = uriTemplateHandler.expand(path, uriVariables).getPath();
        List<ServiceDTO> services = getServices(restRequest);
        for (ServiceDTO serviceDTO : services) {
            try {
                T result = doExecute(method, serviceDTO, path, request, responseType, uriVariables);
                return result;
            } catch (Throwable t) {
                logger.error("Http request failed, uri: {}, method: {}", uri, method, t);

                if (canRetry(t, method)) {
                } else {//biz exception rethrow
                    throw t;
                }
            }
        }

        //all admin server down
        RestApiException e =
                new RestApiException(String.format("Admin servers are unresponsive. meta server address: %s, admin servers: %s",
                        RegisterDomainConsts.getDomain(restRequest.getEnv()), services));
        throw e;
    }

    private <T> ResponseEntity<T> exchangeGet(RestRequest request, String path, ParameterizedTypeReference<T> reference,
                                              Object... uriVariables) {
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }
        String uri = uriTemplateHandler.expand(path, uriVariables).getPath();
        List<ServiceDTO> services = getServices(request);

        for (ServiceDTO serviceDTO : services) {
            try {
                ResponseEntity<T> result =
                        restTemplate.exchange(parseHost(serviceDTO) + path, HttpMethod.GET, null, reference, uriVariables);
                return result;
            } catch (Throwable t) {
                logger.error("Http request failed, uri: {}, method: {}", uri, HttpMethod.GET, t);
                if (canRetry(t, HttpMethod.GET)) {
                } else {// biz exception rethrow
                    throw t;
                }

            }
        }
        //all admin server down
        RestApiException e =
                new RestApiException(String.format("Servers are unresponsive. meta server address: %s, admin servers: %s",
                        RegisterDomainConsts.getDomain(request.getEnv()), services));
        throw e;

    }

    private List<ServiceDTO> getServices(RestRequest condition) {
        List<ServiceDTO> services = serviceAddressLocator.getServiceList(condition);
        if (CollectionUtils.isEmpty(services)) {
            RestApiException e = new RestApiException(String.format("No available server."
                            + " Maybe because of meta server down or all server down. "
                            + "Meta server address: %s"+
                    RegisterDomainConsts.getDomain(condition.getEnv())));
            throw e;
        }

        if (condition.equals(Tool.ADMIN)) {
            List<com.jim.framework.tbox.core.dto.ServiceDTO> result = new ArrayList<>();
            services.forEach(service -> {
                if (service.getAppGroupName().equalsIgnoreCase(condition.getServiceGroupName())) {
                    result.add(service);
                }
            });
            if (result.size() == 0) {
                throw new RuntimeException("no valid instance of goup:" + condition.getServiceGroupName());
            }
            return result;
        }
        return services;
    }

    private <T> T doExecute(HttpMethod method, ServiceDTO service, String path, Object request,
                            Class<T> responseType,
                            Object... uriVariables) {
        T result = null;
        switch (method) {
            case GET:
                result = restTemplate.getForObject(parseHost(service) + path, responseType, uriVariables);
                break;
            case POST:
                result = restTemplate.postForEntity(parseHost(service) + path, request, responseType, uriVariables).getBody();
                break;
            case PUT:
                restTemplate.put(parseHost(service) + path, request, uriVariables);
                break;
            case DELETE:
                restTemplate.delete(parseHost(service) + path, uriVariables);
                break;
            default:
                throw new UnsupportedOperationException(String.format("unsupported http method(method=%s)", method));
        }
        return result;
    }

    private String parseHost(ServiceDTO serviceAddress) {
        return serviceAddress.getHomepageUrl() + "/";
    }

    //post,delete,put请求在admin server处理超时情况下不重试
    private boolean canRetry(Throwable e, HttpMethod method) {
        Throwable nestedException = e.getCause();
        if (method == GET) {
            return nestedException instanceof SocketTimeoutException
                    || nestedException instanceof HttpHostConnectException
                    || nestedException instanceof ConnectTimeoutException;
        } else {
            return nestedException instanceof HttpHostConnectException
                    || nestedException instanceof ConnectTimeoutException;
        }
    }

}
