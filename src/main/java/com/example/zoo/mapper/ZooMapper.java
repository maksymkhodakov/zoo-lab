package com.example.zoo.mapper;

import com.example.zoo.data.ZooData;
import com.example.zoo.dto.ZooDTO;
import com.example.zoo.entity.Country;
import com.example.zoo.entity.Zoo;
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

    public Zoo dataToEntity(ZooData zooData, Country country) {
        return Zoo.builder()
                .name(zooData.getName())
                .square(zooData.getSquare())
                .location(country)
                .build();
    }
}
