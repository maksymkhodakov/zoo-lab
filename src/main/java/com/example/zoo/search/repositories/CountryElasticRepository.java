package com.example.zoo.search.repositories;

import com.example.zoo.search.dto.CountryElasticDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDate;

public interface CountryElasticRepository extends ElasticsearchRepository<CountryElasticDTO, Long> {
    @Query("{\"match\": {\"continent\": {\"query\": \"?0\"}}}")
    Page<CountryElasticDTO> findByContinent(String continent, Pageable pageable);

    Page<CountryElasticDTO> findByCreateDateIsBetween(LocalDate start, LocalDate end, Pageable pageable);
}
