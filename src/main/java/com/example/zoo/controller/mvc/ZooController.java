package com.example.zoo.controller.mvc;

import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.entity.Zoo;
import com.example.zoo.mapper.AnimalMapper;
import com.example.zoo.mapper.CountryMapper;
import com.example.zoo.mapper.ZooMapper;
import com.example.zoo.repository.AnimalRepository;
import com.example.zoo.repository.CountryRepository;
import com.example.zoo.repository.ZooRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/zoo")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZooController {
    ZooRepository zooRepository;
    CountryRepository countryRepository;
    AnimalRepository animalRepository;
    AnimalMapper animalMapper;

    @GetMapping("/getAll")
    public String getAll(Model model) {
        var zoos = zooRepository.findAll()
                .stream()
                .map(ZooMapper::entityToDto)
                .toList();
        model.addAttribute("zooList", zoos);
        return "zooIndex";
    }

    @GetMapping("/create")
    public String create(Model model) {
        var countries = countryRepository.findAll()
                .stream()
                .map(CountryMapper::entityToDto)
                .toList();
        model.addAttribute("listOfCountries", countries);
        return "createZoo";
    }

    @PostMapping("/create")
    public String submitCreate(@RequestParam("zooInput") String text,
                               @RequestParam("squareInput") double square,
                               @RequestParam("countryId") Long id) {
        var country = countryRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        var zoo = Zoo.builder()
                .square(square)
                .name(text)
                .location(country)
                .build();
        zooRepository.saveAndFlush(zoo);
        return "redirect:/zoo/getAll";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        zooRepository.delete(zoo);
        return "redirect:/zoo/getAll";
    }

    @GetMapping("/update/{id}")
    public String updateZoo(@PathVariable Long id, Model model) {
        var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        var countries = countryRepository.findAll()
                .stream()
                .map(CountryMapper::entityToDto)
                .toList();
        var zooDTO = ZooMapper.entityToDto(zoo);
        model.addAttribute("zoo", zooDTO);
        model.addAttribute("listOfCountries", countries);
        return "updateZoo";
    }

    @PostMapping("/update")
    @Transactional
    public String submitUpdateZoo(@RequestParam("nameInput") String name,
                                  @RequestParam("squareInput") double square,
                                  @RequestParam("id") Long id,
                                  @RequestParam("countryId") Long countryId
    ) {
        var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        var country = countryRepository.findById(countryId)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        zoo.setName(name);
        zoo.setSquare(square);
        zoo.setLocation(country);
        return "redirect:/zoo/getAll";
    }

    @GetMapping("/animals/{id}")
    @Transactional
    public String getAll(@PathVariable Long id, Model model) {
        var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        var animals = zoo.getAnimals()
                .stream()
                .map(animalMapper::entityToDto)
                .toList();
        model.addAttribute("zooId", id);
        model.addAttribute("listAnimals", animals);
        return "listAnimals";
    }

    @PostMapping("/deleteAnimal/{animalId}/{zooId}")
    @Transactional
    public String deleteAnimal(@PathVariable Long animalId,
                               @PathVariable Long zooId) {
        var zoo = zooRepository.findById(zooId)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        var animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        zoo.removeAnimal(animal);
        return "redirect:/zoo/animals/" + zooId;
    }

    @GetMapping("/addAnimal/{id}")
    @Transactional
    public String animals(@PathVariable Long id, Model model) {
        var animals = animalRepository.findAll()
                .stream()
                .map(animalMapper::entityToDto)
                .toList();
        model.addAttribute("zooId", id);
        model.addAttribute("listAnimals", animals);
        return "listAnimalsToAdd";
    }

    @PostMapping("/addAnimal")
    @Transactional
    public String animals(@RequestParam("zooId") Long id,
                          @RequestParam("animalId") Long animalId) {
        var zoo = zooRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.ZOO_NOT_FOUND));
        var animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new OperationException(ApiErrors.ANIMAL_NOT_FOUND));
        zoo.addAnimal(animal);
        return "redirect:/zoo/addAnimal/" + id;
    }
}
