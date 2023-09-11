package com.example.zoo.integratons.maps.service;

import com.example.zoo.enums.Continent;
import com.example.zoo.integratons.maps.domain.dto.Coordinates;

public interface ContinentCoordinatesService {
    Coordinates getCoordinatesForContinent(String continentName, String locationName);
    Coordinates continentToCoordinates(Continent continent);
}
