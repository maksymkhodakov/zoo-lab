package com.example.zoo.data;

import com.example.zoo.enums.Continent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountryData {
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    private byte[] flag;

    @NotBlank
    private Continent continent;
}
