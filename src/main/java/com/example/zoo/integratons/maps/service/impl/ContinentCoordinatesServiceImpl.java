package com.example.zoo.integratons.maps.service.impl;

import com.example.zoo.enums.Continent;
import com.example.zoo.integratons.maps.ContinentUtils;
import com.example.zoo.integratons.maps.domain.dto.Coordinates;
import com.example.zoo.integratons.maps.domain.dto.GeocodingResponse;
import com.example.zoo.integratons.maps.service.ContinentCoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContinentCoordinatesServiceImpl implements ContinentCoordinatesService {
    /**
     * Generated in Google console for 1$
     */
    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private final Coordinates defaultCoordinates = new Coordinates(0.0, 0.0);


    /**
     * This method is used to communicate with Google API
     * @param continentName
     * @param locationName
     * @return
     */
    @Override
    public Coordinates getCoordinatesForContinent(String continentName, String locationName) {
        String address = locationName + ", " + continentName;
        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;

        GeocodingResponse response = restTemplate.getForObject(apiUrl, GeocodingResponse.class);

        if (response != null && response.getStatus().equals("OK") && response.getResults().length > 0) {
            double latitude = response.getResults()[0].getGeometry().getLocation().getLat();
            double longitude = response.getResults()[0].getGeometry().getLocation().getLng();
            return new Coordinates(latitude, longitude);
        } else {
            return defaultCoordinates;
        }
    }

    /**
     * This method is used to get a city-precision location, with a random country on the specific continent
     * @param continent
     * @return coordinates
     */
    @Override
    public Coordinates continentToCoordinates(Continent continent) {
        if (Objects.isNull(continent)) {
            return defaultCoordinates;
        }
        final String continentName = continent.getName();
        final String locationName = ContinentUtils.getLocation(continent);
        return getCoordinatesForContinent(continentName, locationName);
    }
}
