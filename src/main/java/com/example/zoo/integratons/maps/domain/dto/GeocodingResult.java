package com.example.zoo.integratons.maps.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodingResult {
    private String formatted_address;
    private Geometry geometry;
}
