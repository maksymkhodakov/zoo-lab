package com.example.zoo.mapper;

import com.example.zoo.data.ZooData;
import com.example.zoo.dto.ZooDTO;
import com.example.zoo.entity.Animal;
import com.example.zoo.entity.Country;
import com.example.zoo.entity.Zoo;
import com.example.zoo.integratons.telegram.domain.dto.ZooTelegramDTO;
import com.example.zoo.search.dto.ZooElasticDTO;
import lombok.experimental.UtilityClass;


@UtilityClass
public class ZooMapper {
    public ZooDTO entityToDto(Zoo zoo){
        return ZooDTO.builder()
                .id(zoo.getId())
                .name(zoo.getName())
                .square(zoo.getSquare())
                .location(CountryMapper.entityToDto(zoo.getLocation()))
                .build();
    }

    public ZooTelegramDTO entityToTelegramDTO(Zoo zoo) {
        return ZooTelegramDTO.builder()
                .id(zoo.getId())
                .name(zoo.getName())
                .square(zoo.getSquare())
                .coordinates(zoo.getLocation().getCoordinates())
                .animalNames(zoo.getAnimals().stream().map(Animal::getName).toList())
                .build();
    }

    public Zoo dataToEntity(ZooData zooData, Country country) {
        return Zoo.builder()
                .name(zooData.getName())
                .square(zooData.getSquare())
                .location(country)
                .build();
    }

    public ZooElasticDTO entityToElasticDTO(Zoo zoo) {
        return ZooElasticDTO.builder()
                .id(zoo.getId())
                .createDate(zoo.getCreateDate().toLocalDateTime().toLocalDate())
                .updateDate(zoo.getLastUpdateDate().toLocalDateTime().toLocalDate())
                .name(zoo.getName())
                .square(zoo.getSquare())
                .location(CountryMapper.entityToElasticDTO(zoo.getLocation()))
                .build();
    }
}
