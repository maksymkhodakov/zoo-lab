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
    ELASTIC_DISABLED("Due to Microsoft student subscription policy, elasticsearch is disabled in prod environment"),

    // Azure Storage
    DOWNLOAD_FILE_FAILED("Error occurred during file downloading from the Azure Storage."),
    UPLOAD_FILE_ERROR("Error occurred during file upload to the Azure Storage"),

    // Azure Queue
    ERROR_OCCURRED_JSON_ERROR("Error occurred while parsing a json from Azure queue");

    private final String message;
}
