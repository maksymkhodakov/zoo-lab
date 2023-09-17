package com.example.zoo.mapper;

import com.example.zoo.data.AnimalData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.entity.Animal;
import com.example.zoo.search.dto.AnimalElasticDTO;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class AnimalMapper {
    public AnimalDTO entityToDto(final Animal animal) {
        return AnimalDTO.builder()
                .id(animal.getId())
                .name(animal.getName())
                .kindAnimal(animal.getKindAnimal())
                .typePowerSupply(animal.getTypePowerSupply())
                .venomous(animal.isVenomous())
                .photo(animal.getPhoto())
                .build();
    }

    public Animal dataToEntity(final AnimalData animal, final byte[] bytes) {
        return Animal.builder()
                .name(animal.getName())
                .kindAnimal(animal.getKindAnimal())
                .typePowerSupply(animal.getTypePowerSupply())
                .venomous(animal.isVenomous())
                .photo(bytes)
                .build();
    }

    public AnimalElasticDTO entityToElasticDTO(Animal animal) {
        return AnimalElasticDTO.builder()
                .id(animal.getId())
                .createDate(animal.getCreateDate().toLocalDateTime().toLocalDate())
                .updateDate(animal.getLastUpdateDate().toLocalDateTime().toLocalDate())
                .name(animal.getName())
                .kindAnimal(animal.getKindAnimal().name())
                .venomous(animal.isVenomous())
                .typePowerSupply(animal.getTypePowerSupply().name())
                .build();
    }
}
