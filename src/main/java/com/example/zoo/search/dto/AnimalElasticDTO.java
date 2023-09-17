package com.example.zoo.search.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;


@Data
@Builder
@Document(indexName = "animalindex")
public class AnimalElasticDTO {
    @Id
    private Long id;

    @Field(type = FieldType.Date, name = "create_date")
    private LocalDate createDate;

    @Field(type = FieldType.Date, name = "update_date")
    private LocalDate updateDate;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "kind_animal")
    private String kindAnimal;

    @Field(type = FieldType.Boolean, name = "venomous")
    private boolean venomous;

    @Field(type = FieldType.Text, name = "type_of_supply")
    private String typePowerSupply;
}
