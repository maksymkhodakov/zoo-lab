package com.example.zoo.integratons.salesforce.service.impl;

import com.example.zoo.integratons.salesforce.config.SalesforceConfig;
import com.example.zoo.integratons.salesforce.domail.LoginResponse;
import com.example.zoo.integratons.salesforce.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RestTemplate restTemplate;
    private final SalesforceConfig salesforceConfig;

    /**
     * Login to Salesforce API and returns response
     * @return
     */
    @Override
    public LoginResponse login() {
        final HttpEntity<MultiValueMap<String, String>> requestEntity = getRequestEntity();
        ResponseEntity<LoginResponse> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(
                    salesforceConfig.getTokenUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    LoginResponse.class);
        } catch (Exception e) {
            log.info("Error occurred: " + e.getMessage());
        }

        return Objects.nonNull(responseEntity) ? responseEntity.getBody() : null;
    }

    private HttpEntity<MultiValueMap<String, String>> getRequestEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new HttpEntity<>(getForm(), httpHeaders);
    }

    private MultiValueMap<String, String> getForm() {
        final MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", salesforceConfig.getUsername());
        form.add("password", salesforceConfig.getPassword());
        form.add("grant_type", "password");
        form.add("client_id", salesforceConfig.getConsumerKey());
        form.add("client_secret", salesforceConfig.getConsumerSecret());
        return form;
    }
}
