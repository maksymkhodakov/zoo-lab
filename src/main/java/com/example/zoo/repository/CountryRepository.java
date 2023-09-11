package com.example.zoo.repository;

import com.example.zoo.entity.Country;
import com.example.zoo.integratons.maps.domain.dto.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("select c.coordinates from Country c where c.coordinates is not null")
    List<Coordinates> getCoordinates();
}
