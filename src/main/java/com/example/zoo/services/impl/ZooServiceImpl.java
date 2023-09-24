package com.example.zoo.services.impl;

import com.example.zoo.data.ZooData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.dto.ZooDTO;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.mapper.AnimalMapper;
import com.example.zoo.mapper.ZooMapper;
import com.example.zoo.repository.AnimalRepository;
import com.example.zoo.repository.CountryRepository;
import com.example.zoo.repository.ZooRepository;
import com.example.zoo.search.dto.ZooElasticDTO;
import com.example.zoo.search.repositories.CountryElasticRepository;
import com.example.zoo.search.repositories.ZooElasticRepository;
import com.example.zoo.services.ZooService;
import com.example.zoo.utils.SearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.zoo.config.CachingConfig.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZooServiceImpl implements ZooService {
    @Value("${enable-elasticsearch}")
    private String enable;

    private final Optional<ZooElasticRepository> zooElasticRepository;
    private final Optional<CountryElasticRepository> countryElasticRepository;
    private final ZooRepository zooRepository;
    private final CountryRepository countryRepository;
    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    private void validateElastic() {
        if (Boolean.FALSE.equals(Boolean.parseBoolean(enable))) {
            throw new OperationException(ApiErrors.ELASTIC_DISABLED);
        }
    }

    @Override
    @Cacheable(value = ZOO, cacheManager = "cacheManager")
    public List<ZooDTO> getAll() {
        return zooRepository.findAll()
                .stream()
                .map(ZooMapper::entityToDto)
                .toList();
    }

    @Override
    public Page<ZooDTO> getAll(SearchDTO searchDTO) {
        return zooRepository.findAll(SearchUtil.getPageable(searchDTO))
                .map(ZooMapper::entityToDto);
    }

    @Override
    @Cacheable(value = ZOO_ELASTIC, cacheManager = "cacheManager")
    public Page<ZooElasticDTO> getAllElastic(SearchDTO searchDTO) {
        validateElastic();
        return zooElasticRepository.get().findAll(SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<ZooElasticDTO> getByName(String name, SearchDTO searchDTO) {
        validateElastic();
        return zooElasticRepository.get().findByName(name, SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<ZooElasticDTO> getBySquareRange(double from, double to, SearchDTO searchDTO) {
        validateElastic();
        return zooElasticRepository.get().findBySquareRange(from, to, SearchUtil.getPageable(searchDTO));
    }

    @Override
    @Transactional
    @CachePut(value = ZOO)
    public void save(ZooData zooData) {
        final var country = countryRepository.findById(zooData.getLocationId())
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        final var zoo = ZooMapper.dataToEntity(zooData, country);
        zooRepository.saveAndFlush(zoo);
        log.info("Zoo with id: " + country.getId() + " created");
    }

    @Override
    @Transactional
    @CachePut(value = ZOO_ELASTIC)
    public void saveElastic(ZooData zooData) {
        validateElastic();
        final var zooElasticDTO = ZooMapper.entityToElasticDTO(ZooMapper.dataToEntity(zooData, null));
        zooElasticRepository.get().save(zooElasticDTO);
        log.info("Zoo with id: " + zooElasticDTO.getId() + " was created in elasticsearch");
    }

    @Override
    @Transactional
    public void update(Long id, ZooData zooData) {
        final var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        final var country = countryRepository.findById(zooData.getLocationId())
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        zoo.setName(zooData.getName());
        zoo.setSquare(zooData.getSquare());
        zoo.setLocation(country);
    }

    @Override
    @Transactional
    public void updateElastic(Long id, ZooData zooData) {
        validateElastic();
        final var zoo = getByIdElastic(id);
        final var country = countryElasticRepository.get().findById(zooData.getLocationId())
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_ELASTIC_NOT_FOUND));
        zoo.setName(zooData.getName());
        zoo.setSquare(zooData.getSquare());
        zoo.setLocation(country);
    }

    @Override
    @Cacheable(value = ZOO, cacheManager = "cacheManager")
    public ZooDTO getById(Long id) {
        return ZooMapper.entityToDto(zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND)));
    }

    @Override
    @Cacheable(value = ZOO_ELASTIC, cacheManager = "cacheManager")
    public ZooElasticDTO getByIdElastic(Long id) {
        validateElastic();
        return zooElasticRepository.get().findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        zooRepository.delete(zoo);
    }

    @Override
    public void deleteElastic(Long id) {
        validateElastic();
        final var zoo = getByIdElastic(id);
        zooElasticRepository.get().delete(zoo);
    }

    @Override
    public List<AnimalDTO> getAllAnimals(Long id) {
        final var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        return zoo.getAnimals()
                .stream().map(animalMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void addAnimal(Long id, Long animalId) {
        var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        var animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        zoo.addAnimal(animal);
    }

    @Override
    @Transactional
    public void deleteAnimal(Long id, Long animalId) {
        var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        var animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        zoo.removeAnimal(animal);
    }
}
