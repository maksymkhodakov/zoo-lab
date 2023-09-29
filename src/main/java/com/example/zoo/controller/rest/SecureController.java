package com.example.zoo.controller.rest;

import com.example.zoo.data.LoginData;
import com.example.zoo.data.RegisterData;
import com.example.zoo.services.SecureBasicAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/secure")
public class SecureController {
    private final SecureBasicAuthenticationService secureBasicAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> registration(@RequestBody RegisterData data){
        return ResponseEntity.ok(secureBasicAuthenticationService.register(data));
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginData data){
        return ResponseEntity.ok(secureBasicAuthenticationService.login(data));
    }
}
