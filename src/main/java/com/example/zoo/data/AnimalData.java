package com.example.zoo.data;

import com.example.zoo.enums.KindAnimal;
import com.example.zoo.enums.TypePowerSupply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnimalData {
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    private KindAnimal kindAnimal;

    @NotNull
    private boolean venomous;

    @NotNull
    private TypePowerSupply typePowerSupply;
}
