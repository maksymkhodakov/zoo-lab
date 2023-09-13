package com.example.zoo.integratons.salesforce.service.impl;

import com.example.zoo.integratons.salesforce.domail.LoginResponse;
import com.example.zoo.integratons.salesforce.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
    private final RestTemplate restTemplate;

    /**
     * Processing a  query
     * @param loginResponse
     * @param query
     * @param response
     * @return
     * @param <T>
     */
    @Override
    public <T> T process(LoginResponse loginResponse, String query, Class<T> response) {
        String url = loginResponse.getInstanceUrl()
                + "/services/data/"
                + "v52.0"
                + "/query/?q={q}";

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("q", query);

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ loginResponse.getAccessToken());

        final HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                response,
                uriVariables);
        log.info("response: " + responseEntity);

        return responseEntity.getBody();
    }
}
