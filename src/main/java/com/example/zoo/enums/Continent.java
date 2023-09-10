package com.example.zoo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * It was a need to predefine exact cities for our zoo system,
 * because only there we found a possibilities to explore new frontiers of zookeeping
 */
@Getter
@RequiredArgsConstructor
public enum Continent {
    Africa("Africa", List.of("Abuja", "Bissau", "Freetown", "Lilongwe", "Tripoli")),
    Australia("Australia", List.of("Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide")),
    NorthAmerica("North America", List.of("Toronto", "New York", "Texas", "San Diego", "Chicago")),
    SouthAmerica("South America", List.of("Lima", "Buenos Aires", "Rio de Janeiro", "Medellín")),
    AntarcticaList("Antarctica", List.of("")),
    Eurasia("Europe", List.of("Kyiv", "Paris", "Lviv", "Donetsk", "Sevastopol"));

    private final String name;
    private final List<String> cities;
}
