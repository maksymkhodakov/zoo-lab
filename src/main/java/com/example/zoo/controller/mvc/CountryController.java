package com.example.zoo.controller.mvc;


import com.example.zoo.entity.Country;
import com.example.zoo.enums.Continent;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.mapper.CountryMapper;
import com.example.zoo.repository.CountryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/country")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryController {
    CountryRepository countryRepository;

    @GetMapping(value = "/getAll")
    public String findAll(Model map) {
        var countries = countryRepository.findAll()
                .stream()
                .map(CountryMapper::entityToDto)
                .toList();
        map.addAttribute("listOfCountries", countries);
        return "indexCountry";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("continents", Continent.values());
        return "createCountry";
    }

    @PostMapping("/create")
    public String submitCreate(@RequestParam("textInput") String textInput,
                               @RequestParam("continent") Continent continent,
                               @RequestParam("fileInput") MultipartFile fileInput) throws IOException {
        if (Objects.nonNull(fileInput)) {
            var country = Country.builder()
                    .name(textInput)
                    .continent(continent)
                    .flag(fileInput.getBytes())
                    .build();
            countryRepository.saveAndFlush(country);
        }
        return "redirect:/country/getAll";
    }

    @PostMapping("/delete")
    public String deleteCountry(@RequestParam Long id) {
        var country = countryRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        countryRepository.delete(country);
        return "redirect:/country/getAll";
    }

    @GetMapping("/update/{id}")
    public String updateCountry(@PathVariable Long id, Model model) {
        var country = countryRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        var countryDto = CountryMapper.entityToDto(country);
        model.addAttribute("country", countryDto);
        model.addAttribute("continents", Continent.values());
        return "updateCountry";
    }

    @PostMapping("/update")
    @Transactional
    public String updateCountry(@RequestParam("name") String name,
                                @RequestParam("id") Long id,
                                @RequestParam("continent") Continent continent,
                                @RequestParam("flag") MultipartFile flag) throws IOException {
        var country = countryRepository.findById(id)
                .orElseThrow(() -> new OperationException(ApiErrors.COUNTRY_NOT_FOUND));
        country.setName(name);
        country.setContinent(continent);
        if (flag.getBytes().length != 0) {
            country.setFlag(flag.getBytes());
        }
        return "redirect:/country/getAll";
    }
}
