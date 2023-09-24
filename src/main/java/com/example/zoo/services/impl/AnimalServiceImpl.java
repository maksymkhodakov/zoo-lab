package com.example.zoo.services.impl;

import com.example.zoo.search.dto.AnimalElasticDTO;
import com.example.zoo.search.repositories.AnimalElasticRepository;
import com.example.zoo.storage.service.StorageService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {
    @Value("${enable-elasticsearch}")
    private String enable;

    private final Optional<AnimalElasticRepository> animalElasticRepository;
    private final AnimalRepository animalRepository;
    private final CountryRepository countryRepository;
    private final AnimalMapper animalMapper;
    private final StorageService storageService;

    private void validateElastic() {
        if (Boolean.FALSE.equals(Boolean.parseBoolean(enable))) {
            throw new OperationException(ApiErrors.ELASTIC_DISABLED);
        }
    }

    @Override
    public List<AnimalDTO> getAll() {
        return animalRepository.findAll()
                .stream()
                .map(animalMapper::entityToDto)
                .toList();
    }

    @Override
    public Page<AnimalElasticDTO> getAllElastic(SearchDTO searchDTO) {
        validateElastic();
        return animalElasticRepository.get().findAll(SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<AnimalElasticDTO> getByNameElastic(String name, SearchDTO searchDTO) {
        validateElastic();
        return animalElasticRepository.get().findByName(name, SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<AnimalElasticDTO> findByKindAnimalAndTypePowerSupply(String kind, String type, SearchDTO searchDTO) {
        validateElastic();
        return animalElasticRepository.get().findByKindAnimalAndTypePowerSupply(kind, type, SearchUtil.getPageable(searchDTO));
    }

    @Override
    public Page<AnimalDTO> getAll(SearchDTO searchDTO) {
        return animalRepository.findAll(SearchUtil.getPageable(searchDTO))
                .map(animalMapper::entityToDto);
    }

    @Override
    @Transactional
    public void save(AnimalData animalData, MultipartFile multipartFile) {
        final var animal = animalMapper.dataToEntity(animalData, multipartFile);
        animalRepository.saveAndFlush(animal);
        log.info("Animal with id: " + animal.getId() + " created");
    }

    @Override
    @Transactional
    public void createElastic(AnimalData animal) {
        validateElastic();
        final var animalElastic = AnimalMapper.entityToElasticDTO(animalMapper.dataToEntity(animal, null));
        final var saved = animalElasticRepository.get().save(animalElastic);
        log.info("Animal with id: " + saved.getId() + " created in elasticsearch");
    }

    @Override
    @Transactional
    public void update(Long id, AnimalData animalData, MultipartFile multipartFile) throws IOException {
        final var animalToUpdate = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        animalToUpdate.setName(animalData.getName());
        animalToUpdate.setVenomous(animalData.isVenomous());
        animalToUpdate.setTypePowerSupply(animalData.getTypePowerSupply());
        animalToUpdate.setKindAnimal(animalData.getKindAnimal());
        animalToUpdate.setPhotoPath(updatePhoto(animalToUpdate.getPhotoPath(), multipartFile));
    }

    private String updatePhoto(String photoPath, MultipartFile multipartFile) {
        if (!Objects.isNull(photoPath) && !photoPath.isEmpty()) {
            storageService.deletePhoto(photoPath);
        }
        return storageService.uploadPhoto(multipartFile);
    }

    @Override
    @Transactional
    public void updateElastic(Long id, AnimalData animalData) {
        validateElastic();
        final var animalToUpdate = getByIdElastic(id);
        animalToUpdate.setName(animalData.getName());
        animalToUpdate.setVenomous(animalData.isVenomous());
        animalToUpdate.setTypePowerSupply(animalData.getTypePowerSupply().name());
        animalToUpdate.setKindAnimal(animalData.getKindAnimal().name());
        animalElasticRepository.get().save(animalToUpdate);
    }

    @Override
    public AnimalDTO getById(Long id) {
        return animalMapper.entityToDto(animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND)));
    }

    @Override
    public AnimalElasticDTO getByIdElastic(Long id) {
        validateElastic();
        return animalElasticRepository.get().findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_ELASTIC_NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final var animalToDelete = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        animalRepository.delete(animalToDelete);
    }

    @Override
    @Transactional
    public void deleteElastic(Long id) {
        validateElastic();
        final var animalToDelete = getByIdElastic(id);
        animalElasticRepository.get().delete(animalToDelete);
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
