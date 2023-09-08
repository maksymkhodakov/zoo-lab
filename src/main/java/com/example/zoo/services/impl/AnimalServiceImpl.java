package com.example.zoo.services.impl;

import com.example.zoo.utils.SearchUtil;
import com.example.zoo.data.AnimalData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.mapper.AnimalMapper;
import com.example.zoo.mapper.CountryMapper;
import com.example.zoo.repository.AnimalRepository;
import com.example.zoo.repository.CountryRepository;
import com.example.zoo.services.AnimalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnimalServiceImpl implements AnimalService {
    AnimalRepository animalRepository;
    CountryRepository countryRepository;

    @Override
    public List<AnimalDTO> getAll() {
        return animalRepository.findAll()
                .stream()
                .map(AnimalMapper::entityToDto)
                .toList();
    }

    @Override
    public Page<AnimalDTO> getAll(SearchDTO searchDTO) {
        return animalRepository.findAll(SearchUtil.getPageable(searchDTO))
                .map(AnimalMapper::entityToDto);
    }

    @Override
    @Transactional
    public void save(AnimalData animalData, MultipartFile multipartFile) throws IOException {
        final var animal = AnimalMapper.dataToEntity(animalData, multipartFile.getBytes());
        animalRepository.saveAndFlush(animal);
        log.info("Animal with id: " + animal.getId() + " created");
    }

    @Override
    @Transactional
    public void update(Long id, AnimalData animalData, MultipartFile multipartFile) throws IOException {
        final var animalToUpdate = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        animalToUpdate.setName(animalData.getName());
        animalToUpdate.setVenomous(animalData.isVenomous());
        animalToUpdate.setTypePowerSupply(animalData.getTypePowerSupply());
        animalToUpdate.setPhoto(multipartFile.getBytes());
        animalToUpdate.setKindAnimal(animalData.getKindAnimal());
    }

    @Override
    public AnimalDTO getById(Long id) {
        return AnimalMapper.entityToDto(animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final var animalToDelete = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        animalRepository.delete(animalToDelete);
    }

    @Override
    public List<CountryDTO> getRelatedCountries(Long id) {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        return animal.getCountries()
                .stream()
                .map(CountryMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteCountry(Long countryId, Long id) {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));

        var country = countryRepository.findById(countryId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));

        animal.removeCountry(country);
    }

    @Override
    @Transactional
    public void addCountry(Long animalId, Long countryId) {
        var animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));

        var country = countryRepository.findById(countryId)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        animal.addCountry(country);
    }
}
