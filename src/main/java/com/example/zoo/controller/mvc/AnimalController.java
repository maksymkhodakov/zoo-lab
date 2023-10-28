package com.example.zoo.controller.mvc;


import com.example.zoo.dto.JQueryAnimalName;
import com.example.zoo.entity.Animal;
import com.example.zoo.enums.KindAnimal;
import com.example.zoo.enums.TypePowerSupply;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.mapper.AnimalMapper;
import com.example.zoo.mapper.CountryMapper;
import com.example.zoo.repository.AnimalRepository;
import com.example.zoo.repository.CountryRepository;
import com.example.zoo.storage.service.StorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/animal")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnimalController {
    public static final String REDIRECT_ANIMAL_GET_ALL = "redirect:/animal/getAll";
    AnimalRepository animalRepository;
    CountryRepository countryRepository;
    StorageService storageService;
    AnimalMapper animalMapper;

    @GetMapping("/findNames")
    public @ResponseBody List<JQueryAnimalName> getNames(String term) {
        return animalRepository.findAll()
                .stream()
                .map(Animal::getName)
                .filter(e->e.contains(term))
                .map(JQueryAnimalName::new)
                .toList();
    }
    @GetMapping("/findAnimalsByName")
    public String getAnimalsByName(@RequestParam("animalName") String name, Model model) {
        var animals = animalRepository.findAll()
                .stream()
                .filter(e->e.getName().contains(name))
                .map(animalMapper::entityToDto)
                .toList();
        model.addAttribute("lostAnimals", animals);
        return "indexAnimals";
    }

    @GetMapping("/getAll")
    public String getAll(Model model) {
        var animals = animalRepository.findAll()
                .stream()
                .map(animalMapper::entityToDto)
                .toList();
        model.addAttribute("lostAnimals", animals);
        return "indexAnimals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("kind", KindAnimal.values());
        model.addAttribute("supply", TypePowerSupply.values());
        return "createAnimal";
    }

    @PostMapping("/create")
    public String submitCreate(@RequestParam("nameInput") String name,
                               @RequestParam("kindAnimal") KindAnimal kindAnimal,
                               @RequestParam(value = "venomous", defaultValue = "false") boolean venomous,
                               @RequestParam("typePowerSupply") TypePowerSupply typePowerSupply,
                               @RequestParam("photo") MultipartFile file) {

        var animal = Animal.builder()
                .name(name)
                .kindAnimal(kindAnimal)
                .venomous(venomous)
                .typePowerSupply(typePowerSupply)
                .photoPath(storageService.uploadPhoto(file))
                .build();

        animalRepository.saveAndFlush(animal);

        return REDIRECT_ANIMAL_GET_ALL;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        animalRepository.delete(animal);
        return REDIRECT_ANIMAL_GET_ALL;
    }

    @GetMapping("/update/{id}")
    public String updateAnimal(@PathVariable Long id, Model model) {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        var animalDTO = animalMapper.entityToDto(animal);
        model.addAttribute("animal", animalDTO);
        model.addAttribute("kind", KindAnimal.values());
        model.addAttribute("supply", TypePowerSupply.values());
        return "updateAnimal";
    }

    @PostMapping("/update")
    @Transactional
    public String submitUpdateAnimal(@RequestParam("id") Long id,
                                     @RequestParam("nameInput") String name,
                                     @RequestParam("kindAnimal") KindAnimal kindAnimal,
                                     @RequestParam(value = "venomous", defaultValue = "false") boolean venomous,
                                     @RequestParam("typePowerSupply") TypePowerSupply typePowerSupply,
                                     @RequestParam("photo") MultipartFile file) throws IOException {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        animal.setName(name);
        animal.setKindAnimal(kindAnimal);
        animal.setVenomous(venomous);
        animal.setTypePowerSupply(typePowerSupply);

        if (file.getBytes().length != 0) {
            animal.setPhotoPath(updatePhoto(animal.getPhotoPath(), file));
        }

        return REDIRECT_ANIMAL_GET_ALL;
    }


    private String updatePhoto(String photoPath, MultipartFile multipartFile) {
        if (!Objects.isNull(photoPath) && !photoPath.isEmpty()) {
            storageService.deletePhoto(photoPath);
        }
        return storageService.uploadPhoto(multipartFile);
    }

    @GetMapping("/countries/{id}")
    @Transactional
    public String countries(@PathVariable Long id, Model model) {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        var countries = animal.getCountries()
                .stream()
                .map(CountryMapper::entityToDto)
                .toList();
        model.addAttribute("listOfCountry", countries);
        model.addAttribute("animalId", id);
        return "allCountry";
    }

    @PostMapping("/deleteCountry/{countryId}/{id}")
    @Transactional
    public String deleteCountries(@PathVariable Long countryId, @PathVariable Long id) {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));

        var country = countryRepository.findById(countryId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));

        animal.removeCountry(country);

        return "redirect:/animal/countries/" + id;
    }

    @GetMapping("/addCountry/{id}")
    @Transactional
    public String addCountries(@PathVariable("id") Long animalId, Model model) {
        var animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));

        var countries = animal.getCountries()
                .stream()
                .map(CountryMapper::entityToDto)
                .collect(Collectors.toCollection(ArrayList::new));

        var allCountries = countryRepository.findAll()
                .stream()
                .map(CountryMapper::entityToDto)
                .collect(Collectors.toCollection(ArrayList::new));

        allCountries.removeAll(countries);

        model.addAttribute("listOfCountry", allCountries);
        model.addAttribute("animalId", animalId);

        return "addCountry";
    }

    @PostMapping("/addCountryToAnimal")
    @Transactional
    public String submitAddCountry(@RequestParam("id") Long animalId,
                                   @RequestParam("countryId") Long countryId) {
        var animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));

        var country = countryRepository.findById(countryId)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        animal.addCountry(country);

        return "redirect:/animal/addCountry/" + animalId;
    }
}
