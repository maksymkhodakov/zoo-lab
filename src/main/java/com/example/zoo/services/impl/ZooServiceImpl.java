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
    CountryRepository countryRepository;
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
    @Transactional
    public void save(ZooData zooData) {
        final var country = countryRepository.findById(zooData.getLocationId())
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        final var zoo = ZooMapper.dataToEntity(zooData, country);
        zooRepository.saveAndFlush(zoo);
        log.info("Zoo with id: " + country.getId() + " created");
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
    public ZooDTO getById(Long id) {
        return ZooMapper.entityToDto(zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        zooRepository.delete(zoo);
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
