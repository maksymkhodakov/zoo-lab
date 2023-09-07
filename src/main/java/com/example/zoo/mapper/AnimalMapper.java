package com.example.zoo.mapper;

import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.entity.Animal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AnimalMapper {
    public AnimalDTO entityToDto(Animal animal){
        return AnimalDTO.builder()
                .id(animal.getId())
                .name(animal.getName())
                .kindAnimal(animal.getKindAnimal())
                .typePowerSupply(animal.getTypePowerSupply())
                .venomous(animal.isVenomous())
                .photo(animal.getPhoto())
                .build();
    }
}
