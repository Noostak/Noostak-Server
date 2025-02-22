package org.noostak.auth.application;

import lombok.extern.slf4j.Slf4j;
import org.noostak.auth.common.exception.RestClientErrorCode;
import org.noostak.auth.common.exception.RestClientException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class RestClient {

    private final RestTemplate restTemplate;

    public RestClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 기본적인 JSON 요청
     */
    public <T, R> T postRequest(String url, R request, Class<T> responseType) {
        try {
            T response = restTemplate.postForObject(url, request, responseType);
            return validate(response);
        } catch (Exception e) {
            throw new RestClientException(RestClientErrorCode.REST_CLIENT_ERROR, e.getMessage());
        }
    }

    /**
     * x-www-form-urlencoded 요청
     */
    public <T> T postRequest(String url, String urlParams, Class<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> entity = new HttpEntity<>(urlParams, headers);
            T response = restTemplate.postForObject(url, entity, responseType);

            return validate(response);
        } catch (Exception e) {
            throw new RestClientException(RestClientErrorCode.REST_CLIENT_ERROR, e.getMessage());
        }
    }

    /**
     * 커스텀 헤더와 함께 JSON 요청
     */
    public <T, R> T postRequest(String url, HttpHeaders headers, R request, Class<T> responseType) {
        try {
            HttpEntity<R> requestHttpEntity = new HttpEntity<>(request, headers);
            T response = restTemplate.postForObject(url, requestHttpEntity, responseType);
            return validate(response);
        } catch (Exception e) {
            throw new RestClientException(RestClientErrorCode.REST_CLIENT_ERROR, e.getMessage());
        }
    }

    /**
     * 커스텀 헤더만 포함된 요청
     */
    public <T> T postRequest(String url, HttpHeaders headers, Class<T> responseType) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            T response = restTemplate.postForObject(url, entity, responseType);

            return validate(response);
        } catch (Exception e) {
            throw new RestClientException(RestClientErrorCode.REST_CLIENT_ERROR, e.getMessage());
        }
    }

    public <T> T getRequest(String url, HttpHeaders headers, Class<T> responseType) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            T response = restTemplate.exchange(url, HttpMethod.GET,entity, responseType).getBody();
            return validate(response);
        } catch (Exception e) {
            throw new RestClientException(RestClientErrorCode.REST_CLIENT_ERROR, e.getMessage());
        }
    }

    private <T> T validate(T response){
        validateIsNull(response);

        return response;
    }

    private <T> void validateIsNull(T response){
        if (response == null) {
            throw new RestClientException(RestClientErrorCode.RESPONSE_IS_NULL);
        }
    }
}


