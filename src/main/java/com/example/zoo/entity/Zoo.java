package com.example.zoo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zoo extends TimestampEntity {

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "zoo_id"),
            inverseJoinColumns = @JoinColumn(name = "animal_id"))
    private List<Animal> animals = new ArrayList<>();

    @ManyToOne
    private Country location;

    private double square;

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        if (!animals.isEmpty()) {
            animals.remove(animal);
        }
    }
}
