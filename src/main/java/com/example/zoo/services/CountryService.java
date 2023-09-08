package com.example.zoo.services;

import com.example.zoo.data.CountryData;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.dto.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CountryService {
    List<CountryDTO> getAll();

    Page<CountryDTO> getAll(SearchDTO searchDTO);

    void save(CountryData countryData, MultipartFile multipartFile) throws IOException;

    void update(Long id, CountryData countryData, MultipartFile multipartFile) throws IOException;

    CountryDTO getById(Long id);

    void delete(Long id);
}
