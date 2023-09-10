package com.example.zoo.converters;

import com.example.zoo.integratons.maps.domain.dto.Coordinates;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Converter
@RequiredArgsConstructor
public class CoordinatesConverter implements AttributeConverter<Coordinates, String> {
    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Coordinates attribute) {
        try {
            if (Objects.nonNull(attribute)) {
                return objectMapper.writeValueAsString(attribute);
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public Coordinates convertToEntityAttribute(String dbData) {
        try {
            if (Objects.nonNull(dbData)) {
                return objectMapper.readValue(dbData, Coordinates.class);
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
