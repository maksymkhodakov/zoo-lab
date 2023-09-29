package com.example.zoo.dto;

import lombok.RequiredArgsConstructor;

import java.util.Map;


@RequiredArgsConstructor
public class GoogleOAuth2UserInfo {
    public static final String GIVEN_NAME = "given_name";
    public static final String EMAIL = "email";
    public static final String FAMILY_NAME = "family_name";
    private final Map<String, Object> attributes;

    public String getName() {
        return attributes.get(GIVEN_NAME).toString();
    }

    public String getEmail() {
        return attributes.get(EMAIL).toString();
    }

    public String getLastName() {
        return attributes.get(FAMILY_NAME).toString();
    }
}
