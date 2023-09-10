package com.example.zoo.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ZooData {
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    private Long locationId;

    @NotBlank
    private double square;
}
