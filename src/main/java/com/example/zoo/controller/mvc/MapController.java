package com.example.zoo.controller.mvc;

import com.example.zoo.integratons.maps.domain.dto.Coordinates;
import com.example.zoo.repository.CountryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapController {
    CountryRepository countryRepository;

    @GetMapping("/all")
    public String coordinates(Model model) {
        List<Coordinates> coordinateList = countryRepository.getCoordinates();
        model.addAttribute("coordinates", coordinateList);
        return "map";
    }
}
