package com.example.zoo.services.impl;

import com.example.zoo.data.CountryData;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.entity.Country;
import com.example.zoo.enums.Continent;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.integratons.maps.service.ContinentCoordinatesService;
import com.example.zoo.mapper.CountryMapper;
import com.example.zoo.repository.CountryRepository;
import com.example.zoo.search.dto.AnimalElasticDTO;
import com.example.zoo.search.dto.CountryElasticDTO;
import com.example.zoo.search.repositories.CountryElasticRepository;
import com.example.zoo.services.CountryService;
import com.example.zoo.utils.SearchUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryServiceImpl implements CountryService {
    CountryRepository countryRepository;
    CountryElasticRepository countryElasticRepository;
    ContinentCoordinatesService coordinatesService;

    @Override
    public List<CountryDTO> getAll() {
        return countryRepository.findAll()
                .stream()
                .map(CountryMapper::entityToDto)
                .toList();
    }

    @Override
    public Page<CountryElasticDTO> getAllElastic(SearchDTO searchDTO) {
        return countryElasticRepository.findAll(SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<CountryElasticDTO> getByContinent(String continent, SearchDTO searchDTO) {
        return countryElasticRepository.findByContinent(continent, SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<CountryElasticDTO> getByDateRange(LocalDate start, LocalDate end, SearchDTO searchDTO) {
        return countryElasticRepository.findByCreateDateIsBetween(start, end, SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<CountryDTO> getAll(SearchDTO searchDTO) {
        return countryRepository.findAll(SearchUtil.getPageable(searchDTO))
                .map(CountryMapper::entityToDto);
    }

    @Override
    @Transactional
    public void save(CountryData countryData, MultipartFile multipartFile) throws IOException {
        final var country = CountryMapper.dataToEntity(countryData, multipartFile.getBytes());
        country.setCoordinates(coordinatesService.continentToCoordinates(countryData.getContinent()));
        countryRepository.saveAndFlush(country);
        log.info("Country with id: " + country.getId() + " created");
        saveElastic(country);
    }

    @Transactional
    public void saveElastic(Country country) {
        final var countryElastic = CountryMapper.entityToElasticDTO(country);
        countryElasticRepository.save(countryElastic);
        log.info("Country with id " + country.getId() + " created in elasticsearch");
    }

    @Override
    @Transactional
    public void update(Long id, CountryData countryData, MultipartFile multipartFile) throws IOException {
        final var country = countryRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        country.setName(countryData.getName());
        country.setContinent(countryData.getContinent());
        country.setCoordinates(coordinatesService.continentToCoordinates(countryData.getContinent()));
        country.setFlag(multipartFile.getBytes());
    }

    @Override
    @Transactional
    public void updateElastic(Long id, CountryData countryData) {
        final var country = getByIdElastic(id);
        country.setName(countryData.getName());
        country.setContinent(countryData.getContinent().getName());
        country.setCoordinates(coordinatesService.continentToCoordinates(countryData.getContinent()).toString());
        countryElasticRepository.save(country);
    }

    @Override
    public CountryDTO getById(Long id) {
        return CountryMapper.entityToDto(countryRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND)));
    }

    @Override
    public CountryElasticDTO getByIdElastic(Long id) {
        return countryElasticRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_ELASTIC_NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final var country = countryRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        countryRepository.delete(country);
    }

    @Override
    public void deleteElastic(Long id) {
        final var country = getByIdElastic(id);
        countryElasticRepository.save(country);
    }
}
