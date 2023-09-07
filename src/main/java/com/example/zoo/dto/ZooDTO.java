package com.example.zoo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZooDTO {

    private Long id;

    private String name;

    private CountryDTO location;

    private double square;
}
