package com.example.zoo.dto;


import com.example.zoo.enums.KindAnimal;
import com.example.zoo.enums.TypePowerSupply;
import lombok.*;
import org.apache.tomcat.util.codec.binary.Base64;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalDTO {

    private Long id;

    private String name;

    private KindAnimal kindAnimal;

    private boolean venomous;

    private TypePowerSupply typePowerSupply;

    private byte[] photo;

    public String generateBase64Image() {
        return Base64.encodeBase64String(photo);
    }

}
