package com.example.zoo.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiErrors {

    // mariadb
    ZOO_NOT_FOUND("Zoo with provided id was not found!"),
    COUNTRY_NOT_FOUND("Country with provided id was not found!"),
    ANIMAL_NOT_FOUND("Animal with provided id was not found!"),

    // elasticsearch
    ANIMAL_ELASTIC_NOT_FOUND("Animal with provided id was not found in Elasticsearch storage"),
    COUNTRY_ELASTIC_NOT_FOUND("Country with provided id was not found in Elasticsearch storage"),
    ZOO_ELASTIC_NOT_FOUND("Zoo with provided id was not found in Elasticsearch storage"),

    ELASTIC_DISABLED("Due to Microsoft student subscription policy, elasticsearch is disabled in prod environment");

    private final String message;
}
