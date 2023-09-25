package com.example.zoo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends TimestampEntity implements UserDetails, OAuth2User {

    private String email;

    private String password;

    private String name;

    private String lastName;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Features> authorities;

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public String getUsername() {
        return email;
    }
}
