package com.example.zoo.controller.rest;

import com.example.zoo.data.AnimalData;
import com.example.zoo.dto.AnimalDTO;
import com.example.zoo.dto.CountryDTO;
import com.example.zoo.dto.ResponseDTO;
import com.example.zoo.dto.SearchDTO;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.search.dto.AnimalElasticDTO;
import com.example.zoo.services.AnimalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/animal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnimalRestController {
    AnimalService animalService;

    @GetMapping("/getAll")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<List<AnimalDTO>> getAll() {
        return ResponseDTO.ofData(animalService.getAll(), ResponseDTO.ResponseStatus.OK);
    }

    @GetMapping("/elastic/getAll")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_ADMIN)")
    public ResponseDTO<Page<AnimalElasticDTO>> getAllElastic(@RequestBody SearchDTO searchDTO) {
        return ResponseDTO.ofData(animalService.getAllElastic(searchDTO), ResponseDTO.ResponseStatus.OK);
    }

    @GetMapping("/pagination/getAll")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<Page<AnimalDTO>> paginationGetAll(@RequestBody SearchDTO searchDTO) {
        return ResponseDTO.ofData(animalService.getAll(searchDTO), ResponseDTO.ResponseStatus.OK);
    }

    @GetMapping("/elastic/findByName/{name}")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_ADMIN)")
    public ResponseDTO<Page<AnimalElasticDTO>> getByName(@PathVariable String name, @RequestBody SearchDTO searchDTO) {
        try {
            return ResponseDTO.ofData(animalService.getByNameElastic(name, searchDTO), ResponseDTO.ResponseStatus.OK);
        } catch (OperationException e) {
            return ResponseDTO.ofData(null, ResponseDTO.ResponseStatus.ERROR);
        }
    }

    @GetMapping("/elastic/findByKindAndType")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_ADMIN)")
    public ResponseDTO<Page<AnimalElasticDTO>> getByKindAndType(@RequestParam String kind,
                                                                @RequestParam String type,
                                                                @RequestBody SearchDTO searchDTO) {
        try {
            return ResponseDTO.ofData(animalService.findByKindAnimalAndTypePowerSupply(kind, type, searchDTO), ResponseDTO.ResponseStatus.OK);
        } catch (OperationException e) {
            return ResponseDTO.ofData(null, ResponseDTO.ResponseStatus.ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<AnimalDTO> getById(@PathVariable Long id) {
        try {
            return ResponseDTO.ofData(animalService.getById(id), ResponseDTO.ResponseStatus.OK);
        } catch (OperationException e) {
            return ResponseDTO.ofData(null, ResponseDTO.ResponseStatus.ERROR);
        }
    }

    @GetMapping("/elastic/{id}")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_ADMIN)")
    public ResponseDTO<AnimalElasticDTO> getByIdElastic(@PathVariable Long id) {
        try {
            return ResponseDTO.ofData(animalService.getByIdElastic(id), ResponseDTO.ResponseStatus.OK);
        } catch (OperationException e) {
            return ResponseDTO.ofData(null, ResponseDTO.ResponseStatus.ERROR);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<Void> create(@RequestPart("data") AnimalData animalData,
                                    @RequestPart("file") MultipartFile multipartFile) {
        try {
            animalService.save(animalData, multipartFile);
        } catch (IOException e) {
            return ResponseDTO.error(e.getMessage());
        }
        return ResponseDTO.ok();
    }

    @PostMapping("/elastic/create")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_ADMIN)")
    public ResponseDTO<Void> createElastic(@RequestPart("data") AnimalData animalData) {
        try {
            animalService.createElastic(animalData);
        } catch (OperationException e) {
            return ResponseDTO.error(e.getMessage());
        }
        return ResponseDTO.ok();
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<Void> update(@RequestPart("id") Long id,
                                    @RequestPart("data") AnimalData animalData,
                                    @RequestPart("file") MultipartFile multipartFile) {
        try {
            animalService.update(id, animalData, multipartFile);
        } catch (IOException e) {
            return ResponseDTO.error(e.getMessage());
        }
        return ResponseDTO.ok();
    }

    @PutMapping("/elastic/update")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_ADMIN)")
    public ResponseDTO<Void> updateElastic(@RequestPart("id") Long id, @RequestPart("data") AnimalData animalData) {
        try {
            animalService.updateElastic(id, animalData);
        } catch (OperationException e) {
            return ResponseDTO.error(e.getMessage());
        }
        return ResponseDTO.ok();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<Void> delete(@RequestParam Long id) {
        try {
            animalService.delete(id);
        } catch (OperationException e) {
            return ResponseDTO.error(e.getMessage());
        }
        return ResponseDTO.ok();
    }

    @DeleteMapping("/elastic/delete")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_ADMIN)")
    public ResponseDTO<Void> deleteElastic(@RequestParam Long id) {
        try {
            animalService.deleteElastic(id);
        } catch (OperationException e) {
            return ResponseDTO.error(e.getMessage());
        }
        return ResponseDTO.ok();
    }

    @GetMapping("/countries/{id}")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<List<CountryDTO>> countries(@PathVariable Long id) {
        return ResponseDTO.ofData(animalService.getRelatedCountries(id), ResponseDTO.ResponseStatus.OK);
    }

    @DeleteMapping("/deleteCountry/{countryId}/{id}")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<Void> deleteCountry(@PathVariable Long countryId, @PathVariable Long id) {
        try {
            animalService.deleteCountry(countryId, id);
        } catch (OperationException e) {
            return ResponseDTO.error(e.getMessage());
        }
        return ResponseDTO.ok();
    }

    @PutMapping("/addCountryToAnimal")
    @PreAuthorize("hasPermission(null, T(com.example.zoo.enums.Privilege).ROLE_BASIC_USER)")
    public ResponseDTO<Void> addCountry(@RequestParam("id") Long animalId,
                                        @RequestParam("countryId") Long countryId) {
        try {
            animalService.addCountry(animalId, countryId);
        } catch (OperationException e) {
            return ResponseDTO.error(e.getMessage());
        }
        return ResponseDTO.ok();
    }
}
