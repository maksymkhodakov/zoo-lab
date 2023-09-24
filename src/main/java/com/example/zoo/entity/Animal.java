package com.example.zoo.entity;


import com.example.zoo.enums.KindAnimal;
import com.example.zoo.enums.TypePowerSupply;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Animal extends TimestampEntity {

    private String name;

    @Enumerated(value = EnumType.STRING)
    private KindAnimal kindAnimal;

    private boolean venomous;

    @Enumerated(value = EnumType.STRING)
    private TypePowerSupply typePowerSupply;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"))
    private List<Country> countries = new ArrayList<>();

    /**
     * for storing photos for animals Azure Blob Storage is used
     */
    @Column(name = "photo_path")
    private String photoPath;

    public void addCountry(Country country) {
        countries.add(country);
    }

    public void removeCountry(Country country) {
        if (!countries.isEmpty()) {
            countries.remove(country);
        }
    }
}
