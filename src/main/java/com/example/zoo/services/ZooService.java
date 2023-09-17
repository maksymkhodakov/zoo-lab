package com.example.zoo.services;

import com.example.zoo.data.ZooData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.dto.ZooDTO;
import com.example.zoo.search.dto.ZooElasticDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ZooService {
    List<ZooDTO> getAll();

    Page<ZooDTO> getAll(SearchDTO searchDTO);

    Page<ZooElasticDTO> getAllElastic(SearchDTO searchDTO);

    Page<ZooElasticDTO> getByName(String name, SearchDTO searchDTO);

    Page<ZooElasticDTO> getBySquareRange(double from, double to, SearchDTO searchDTO);

    void save(ZooData zooData);

    void update(Long id, ZooData zooData);

    void updateElastic(Long id, ZooData zooData);

    ZooDTO getById(Long id);

    ZooElasticDTO getByIdElastic(Long id);

    void delete(Long id);

    void deleteElastic(Long id);

    List<AnimalDTO> getAllAnimals(Long id);

    void addAnimal(Long id, Long animalId);

    void deleteAnimal(Long id, Long animalId);
}
