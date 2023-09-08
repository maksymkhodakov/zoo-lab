package com.example.zoo.mapper;

import com.example.zoo.data.CountryData;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.entity.Country;
import lombok.experimental.UtilityClass;


@UtilityClass
public class CountryMapper {
    public CountryDTO entityToDto(Country country){
        return CountryDTO.builder()
                .id(country.getId())
                .name(country.getName())
                .continent(country.getContinent())
                .flag(country.getFlag())
                .build();
    }

    public Country dataToEntity(CountryData countryData, byte[] bytes) {
        return Country.builder()
                .name(countryData.getName())
                .continent(countryData.getContinent())
                .flag(bytes)
                .build();
    }
}
