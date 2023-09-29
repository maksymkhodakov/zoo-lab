package com.example.zoo.converters;

import com.example.zoo.dto.FeatureDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@Converter
@RequiredArgsConstructor
public class FeatureConverter implements AttributeConverter<List<FeatureDTO>, String> {
    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<FeatureDTO> attribute) {
        try {
            return Objects.isNull(attribute) ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FeatureDTO> convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
