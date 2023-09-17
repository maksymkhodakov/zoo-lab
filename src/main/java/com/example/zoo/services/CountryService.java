package com.example.zoo.services;

import com.example.zoo.data.CountryData;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.search.dto.CountryElasticDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface CountryService {
    List<CountryDTO> getAll();

    Page<CountryElasticDTO> getAllElastic(SearchDTO searchDTO);

    Page<CountryElasticDTO> getByContinent(String continent, SearchDTO searchDTO);

    Page<CountryElasticDTO> getByDateRange(LocalDate start, LocalDate end, SearchDTO searchDTO);

    Page<CountryDTO> getAll(SearchDTO searchDTO);

    void updateElastic(Long id, CountryData countryData);

    void save(CountryData countryData, MultipartFile multipartFile) throws IOException;

    void update(Long id, CountryData countryData, MultipartFile multipartFile) throws IOException;

    CountryDTO getById(Long id);

    CountryElasticDTO getByIdElastic(Long id);

    void delete(Long id);

    void deleteElastic(Long id);
}
