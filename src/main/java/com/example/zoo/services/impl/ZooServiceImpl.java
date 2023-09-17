package com.example.zoo.services.impl;

import com.example.zoo.data.ZooData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.dto.ZooDTO;
import com.example.zoo.entity.Zoo;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZooServiceImpl implements ZooService {
    ZooRepository zooRepository;
    ZooElasticRepository zooElasticRepository;
    CountryRepository countryRepository;
    CountryElasticRepository countryElasticRepository;
    AnimalRepository animalRepository;

    @Override
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
    public Page<ZooElasticDTO> getAllElastic(SearchDTO searchDTO) {
        return zooElasticRepository.findAll(SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<ZooElasticDTO> getByName(String name, SearchDTO searchDTO) {
        return zooElasticRepository.findByName(name, SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<ZooElasticDTO> getBySquareRange(double from, double to, SearchDTO searchDTO) {
        return zooElasticRepository.findBySquareRange(from, to, SearchUtil.getPageable(searchDTO));
    }

    @Override
    @Transactional
    public void save(ZooData zooData) {
        final var country = countryRepository.findById(zooData.getLocationId())
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        final var zoo = ZooMapper.dataToEntity(zooData, country);
        zooRepository.saveAndFlush(zoo);
        log.info("Zoo with id: " + country.getId() + " created");
        saveElastic(zoo);
    }

    @Transactional
    public void saveElastic(Zoo zoo) {
        final var zooElasticDTO = ZooMapper.entityToElasticDTO(zoo);
        zooElasticRepository.save(zooElasticDTO);
        log.info("Zoo with id: " + zoo.getId() + " was created in elasticsearch");
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
    public void updateElastic(Long id, ZooData zooData) {
        final var zoo = getByIdElastic(id);
        final var country = countryElasticRepository.findById(zooData.getLocationId())
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_ELASTIC_NOT_FOUND));
        zoo.setName(zooData.getName());
        zoo.setSquare(zooData.getSquare());
        zoo.setLocation(country);
    }

    @Override
    public ZooDTO getById(Long id) {
        return ZooMapper.entityToDto(zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND)));
    }

    @Override
    public ZooElasticDTO getByIdElastic(Long id) {
        return zooElasticRepository.findById(id)
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
        final var zoo = getByIdElastic(id);
        zooElasticRepository.delete(zoo);
    }

    @Override
    public List<AnimalDTO> getAllAnimals(Long id) {
        final var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        return zoo.getAnimals()
                .stream().map(AnimalMapper::entityToDto)
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
