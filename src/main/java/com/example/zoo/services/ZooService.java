package com.example.zoo.services;

import com.example.zoo.data.ZooData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.dto.ZooDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ZooService {
    List<ZooDTO> getAll();

    Page<ZooDTO> getAll(SearchDTO searchDTO);

    void save(ZooData zooData, Long countryId);

    void update(Long id, ZooData zooData, Long countryId);

    ZooDTO getById(Long id);

    void delete(Long id);

    List<AnimalDTO> getAllAnimals(Long id);

    void addAnimal(Long id, Long animalId);

    void deleteAnimal(Long id, Long animalId);
}
