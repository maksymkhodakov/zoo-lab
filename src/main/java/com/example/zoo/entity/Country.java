package com.example.zoo.entity;

import com.example.zoo.enums.Continent;
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
public class Country extends TimestampEntity {

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Continent continent;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private byte[] flag;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Zoo> zoos = new ArrayList<>();
}
