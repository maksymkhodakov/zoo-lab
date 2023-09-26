package com.example.zoo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilter {
    private final AuthenticationProvider authenticationProvider;
    private final CustomSecurityFilter customSecurityFilter;
    //private final ZooOAuth2UserService zooOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(e ->
                        e.requestMatchers( "/secure/**","/oauth2/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
/*                .oauth2Login(e -> e.authorizationEndpoint(f -> f.baseUri("/oauth2/authorize"))
                        .redirectionEndpoint(f -> f.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(f -> f.userService(zooOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )*/
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(customSecurityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
