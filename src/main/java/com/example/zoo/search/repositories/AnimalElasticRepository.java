package com.example.zoo.search.repositories;

import com.example.zoo.search.dto.AnimalElasticDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AnimalElasticRepository extends ElasticsearchRepository<AnimalElasticDTO, Long> {
    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    Page<AnimalElasticDTO> findByName(String name, Pageable pageable);

    Page<AnimalElasticDTO> findByKindAnimalAndTypePowerSupply(String kinAnimal, String type, Pageable pageable);
}
