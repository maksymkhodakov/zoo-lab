package com.example.zoo.integratons.maps.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private double lat; // Latitude
    private double lng; // Longitude
}
