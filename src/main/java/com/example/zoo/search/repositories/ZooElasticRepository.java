package com.example.zoo.search.repositories;

import com.example.zoo.search.dto.ZooElasticDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@ConditionalOnProperty(name = "enable-elasticsearch", havingValue = "true")
public interface ZooElasticRepository extends ElasticsearchRepository<ZooElasticDTO, Long> {
    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    Page<ZooElasticDTO> findByName(String name, Pageable pageable);


    @Query("{\"bool\": {\"must\": [{\"range\": {\"square\": {\"gte\": ?0, \"lte\": ?1}}}]}}")
    Page<ZooElasticDTO> findBySquareRange(double from, double to, Pageable pageable);
}
