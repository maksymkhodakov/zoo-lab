package com.example.zoo.services.impl;

import com.example.zoo.data.LoginData;
import com.example.zoo.data.RegisterData;
import com.example.zoo.entity.User;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.repository.UserRepository;
import com.example.zoo.services.JwtService;
import com.example.zoo.services.SecureBasicAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Class for authentication with Bearer token, OAuth2 security would be slightly different
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecureBasicAuthenticationServiceImpl implements SecureBasicAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean register(RegisterData data) {
        try {
            var user = User
                    .builder()
                    .email(data.getEmail())
                    .password(passwordEncoder.encode(data.getPassword()))
                    .authorities(List.of(data.getAuthorities()))
                    .name(data.getFirstName())
                    .lastName(data.getLastName())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
            userRepository.saveAndFlush(user);
        } catch (Exception e) {
            log.info("Error occurred wile registering user");
            return false;
        }
        return true;
    }

    @Override
    public String login(LoginData data) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
        final User user = userRepository.loadByUsername(data.getEmail())
                .orElseThrow(() -> new OperationException(ApiErrors.USER_NOT_FOUND));
        return jwtService.generateToken(user);
    }
}
