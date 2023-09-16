package com.example.zoo.search.repositories;

import com.example.zoo.search.dto.CountryElasticDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CountryElasticRepository extends ElasticsearchRepository<CountryElasticDTO, Long> {
}
