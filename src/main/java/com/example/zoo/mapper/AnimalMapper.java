package com.example.zoo.mapper;

import com.example.zoo.data.AnimalData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.entity.Animal;
import com.example.zoo.search.dto.AnimalElasticDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class AnimalMapper {

    public AnimalDTO entityToDto(final Animal animal) {
        return AnimalDTO.builder()
                .id(animal.getId())
                .name(animal.getName())
                .kindAnimal(animal.getKindAnimal())
                .typePowerSupply(animal.getTypePowerSupply())
                .venomous(animal.isVenomous())
                .photo(new byte[0])
                .build();
    }

    public Animal dataToEntity(final AnimalData animal, final MultipartFile multipartFile) {
        return Animal.builder()
                .name(animal.getName())
                .kindAnimal(animal.getKindAnimal())
                .typePowerSupply(animal.getTypePowerSupply())
                .venomous(animal.isVenomous())
                .photoPath(multipartFile.getOriginalFilename())
                .build();
    }

    public static AnimalElasticDTO entityToElasticDTO(Animal animal) {
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
