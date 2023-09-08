package com.example.zoo.services;

import com.example.zoo.data.AnimalData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.dto.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnimalService {

    List<AnimalDTO> getAll();

    Page<AnimalDTO> getAll(SearchDTO searchDTO);

    void save(AnimalData animalData, MultipartFile multipartFile) throws IOException;

    void update(Long id, AnimalData animalData, MultipartFile multipartFile) throws IOException;

    AnimalDTO getById(Long id);

    void delete(Long id);

    List<CountryDTO> getRelatedCountries(Long id);

    void deleteCountry(Long countryId, Long id);

    void addCountry(Long animalId, Long countryId);
}
