package com.example.zoo.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiErrors {

    ZOO_NOT_FOUND("Zoo with provided id was not found!"),
    COUNTRY_NOT_FOUND("Country with provided id was not found!"),
    ANIMAL_NOT_FOUND("Animal with provided id was not found!"),;

    private final String message;
}
