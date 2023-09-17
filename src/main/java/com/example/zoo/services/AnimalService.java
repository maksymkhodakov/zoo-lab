package com.example.zoo.services;

import com.example.zoo.data.AnimalData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.entity.Animal;
import com.example.zoo.search.dto.AnimalElasticDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnimalService {

    List<AnimalDTO> getAll();

    Page<AnimalElasticDTO> getAllElastic(SearchDTO searchDTO);

    Page<AnimalElasticDTO> getByNameElastic(String name, SearchDTO searchDTO);

    Page<AnimalElasticDTO> findByKindAnimalAndTypePowerSupply(String kind, String type, SearchDTO searchDTO);

    Page<AnimalDTO> getAll(SearchDTO searchDTO);

    void save(AnimalData animalData, MultipartFile multipartFile) throws IOException;

    void createElastic(AnimalData animal);

    void update(Long id, AnimalData animalData, MultipartFile multipartFile) throws IOException;

    void updateElastic(Long id, AnimalData animalData);

    AnimalDTO getById(Long id);

    AnimalElasticDTO getByIdElastic(Long id);

    void delete(Long id);

    void deleteElastic(Long id);

    List<CountryDTO> getRelatedCountries(Long id);

    void deleteCountry(Long countryId, Long id);

    void addCountry(Long animalId, Long countryId);
}
