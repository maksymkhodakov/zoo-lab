package com.example.zoo.search.repositories;

import com.example.zoo.search.dto.ZooElasticDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ZooElasticRepository extends ElasticsearchRepository<ZooElasticDTO, Long> {
}
