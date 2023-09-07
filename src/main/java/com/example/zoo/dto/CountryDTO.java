package com.example.zoo.dto;

import com.example.zoo.enums.Continent;
import lombok.*;
import org.apache.tomcat.util.codec.binary.Base64;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CountryDTO {

    private Long id;

    private String name;

    private byte[] flag;

    private Continent continent;

    public String generateBase64Image() {
        return Base64.encodeBase64String(getFlag());
    }
}
