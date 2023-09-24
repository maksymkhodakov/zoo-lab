package com.example.zoo.services.impl;

import com.example.zoo.data.CountryData;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.integratons.maps.service.ContinentCoordinatesService;
import com.example.zoo.mapper.CountryMapper;
import com.example.zoo.repository.CountryRepository;
import com.example.zoo.search.dto.CountryElasticDTO;
import com.example.zoo.search.repositories.CountryElasticRepository;
import com.example.zoo.services.CountryService;
import com.example.zoo.utils.SearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.zoo.config.CachingConfig.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    @Value("${enable-elasticsearch}")
    private String enable;

    private final Optional<CountryElasticRepository> countryElasticRepository;
    private final CountryRepository countryRepository;
    private final ContinentCoordinatesService coordinatesService;

    private void validateElastic() {
        if (Boolean.FALSE.equals(Boolean.parseBoolean(enable))) {
            throw new OperationException(ApiErrors.ELASTIC_DISABLED);
        }
    }

    @Override
    @Cacheable(value = COUNTRIES, cacheManager = "cacheManager")
    public List<CountryDTO> getAll() {
        return countryRepository.findAll()
                .stream()
                .map(CountryMapper::entityToDto)
                .toList();
    }

    @Override
    @Cacheable(value = COUNTRIES_ELASTIC, cacheManager = "cacheManager")
    public Page<CountryElasticDTO> getAllElastic(SearchDTO searchDTO) {
        validateElastic();
        return countryElasticRepository.get().findAll(SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<CountryElasticDTO> getByContinent(String continent, SearchDTO searchDTO) {
        validateElastic();
        return countryElasticRepository.get().findByContinent(continent, SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<CountryElasticDTO> getByDateRange(LocalDate start, LocalDate end, SearchDTO searchDTO) {
        validateElastic();
        return countryElasticRepository.get().findByCreateDateIsBetween(start, end, SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<CountryDTO> getAll(SearchDTO searchDTO) {
        return countryRepository.findAll(SearchUtil.getPageable(searchDTO))
                .map(CountryMapper::entityToDto);
    }

    @Override
    @Transactional
    @CachePut(value = COUNTRIES)
    public void save(CountryData countryData, MultipartFile multipartFile) throws IOException {
        final var country = CountryMapper.dataToEntity(countryData, multipartFile.getBytes());
        country.setCoordinates(coordinatesService.continentToCoordinates(countryData.getContinent()));
        countryRepository.saveAndFlush(country);
        log.info("Country with id: " + country.getId() + " created");
    }

    @Override
    @Transactional
    @CachePut(value = COUNTRIES_ELASTIC)
    public void saveElastic(CountryData countryData) {
        validateElastic();
        final var countryElastic = CountryMapper.entityToElasticDTO(CountryMapper.dataToEntity(countryData, null));
        countryElasticRepository.get().save(countryElastic);
        log.info("Country with id " + countryElastic.getId() + " created in elasticsearch");
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
        validateElastic();
        final var country = getByIdElastic(id);
        country.setName(countryData.getName());
        country.setContinent(countryData.getContinent().getName());
        country.setCoordinates(coordinatesService.continentToCoordinates(countryData.getContinent()).toString());
        countryElasticRepository.get().save(country);
    }

    @Override
    @Cacheable(value = COUNTRIES, cacheManager = "cacheManager")
    public CountryDTO getById(Long id) {
        return CountryMapper.entityToDto(countryRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND)));
    }

    @Override
    @Cacheable(value = COUNTRIES_ELASTIC, cacheManager = "cacheManager")
    public CountryElasticDTO getByIdElastic(Long id) {
        validateElastic();
        return countryElasticRepository.get().findById(id)
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
    @Transactional
    public void deleteElastic(Long id) {
        validateElastic();
        final var country = getByIdElastic(id);
        countryElasticRepository.get().save(country);
    }
}
