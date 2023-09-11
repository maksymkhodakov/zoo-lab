package com.example.zoo.integratons.telegram.domain.dto;

import com.example.zoo.integratons.maps.domain.dto.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Is used in Telegram API integration in order not to exceed max message length
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZooTelegramDTO {
    private Long id;
    private String name;
    private double square;
    private Coordinates coordinates;
    private List<String> animalNames;
}
