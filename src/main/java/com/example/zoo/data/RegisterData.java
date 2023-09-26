package com.example.zoo.data;

import com.example.zoo.dto.FeatureDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterData {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private FeatureDTO authorities;
}
