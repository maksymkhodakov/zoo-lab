package com.example.zoo.services;

import com.example.zoo.data.LoginData;
import com.example.zoo.data.RegisterData;

public interface SecureBasicAuthenticationService {
    boolean register(RegisterData data);
    String login(LoginData data);
}
