package com.example.zoo.services.impl;

import com.example.zoo.dto.FeatureDTO;
import com.example.zoo.dto.GoogleOAuth2UserInfo;
import com.example.zoo.dto.UserDTO;
import com.example.zoo.entity.User;
import com.example.zoo.enums.Privilege;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ZooOAuth2UserService extends DefaultOAuth2UserService {
    @Value("${oauth2.tempo.password}")
    private String tempoPass;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return processUser(super.loadUser(userRequest));
    }

    private OAuth2User processUser(OAuth2User oAuth2User) {
        final GoogleOAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        final String emailFromOAuth2 = oAuth2UserInfo.getEmail();
        if (Objects.isNull(emailFromOAuth2) || emailFromOAuth2.isEmpty()) {
            throw new OperationException(ApiErrors.NO_EMAIL);
        }
        final var userFromDb = userRepository.loadByUsername(oAuth2UserInfo.getEmail());
        if (userFromDb.isPresent()) {
            return UserDTO.generate(update(userFromDb.get(), oAuth2UserInfo), oAuth2User.getAttributes());
        } else {
            return UserDTO.generate(registerNewUser(oAuth2UserInfo), oAuth2User.getAttributes());
        }
    }

    private User registerNewUser(GoogleOAuth2UserInfo oAuth2UserInfo) {
        var user = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .password(passwordEncoder.encode(tempoPass))
                .accountNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .credentialsNonExpired(true)
                // for testing purposes
                .authorities(List.of(
                        new FeatureDTO(Privilege.ROLE_BASIC_USER),
                        new FeatureDTO(Privilege.ROLE_STANDARD_USER),
                        new FeatureDTO(Privilege.ROLE_PREMIUM_USER),
                        new FeatureDTO(Privilege.ROLE_ADMIN)
                ))
                .lastName(oAuth2UserInfo.getLastName())
                .name(oAuth2UserInfo.getName())
                .build();
        return userRepository.saveAndFlush(user);
    }

    private User update(User existingUser, GoogleOAuth2UserInfo oAuth2UserInfo) {
        existingUser.setEmail(oAuth2UserInfo.getEmail());
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setLastName(oAuth2UserInfo.getLastName());
        return userRepository.save(existingUser);
    }
}
