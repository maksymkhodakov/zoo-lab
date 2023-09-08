package com.example.zoo.utils;

import com.example.zoo.dto.SearchDTO;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class SearchUtil {
    public Pageable getPageable(SearchDTO searchDTO) {
        return PageRequest.of(searchDTO.getPage(), searchDTO.getSize());
    }
}
