package com.example.zoo.mapper;
import com.example.zoo.dto.ZooDTO;
import com.example.zoo.entity.Zoo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ZooMapper {
    public ZooDTO EntityToDto(Zoo zoo){
        return ZooDTO.builder()
                .id(zoo.getId())
                .name(zoo.getName())
                .square(zoo.getSquare())
                .location(CountryMapper.entityToDto(zoo.getLocation()))
                .build();
    }
}
