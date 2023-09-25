package com.example.zoo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Entity
@Table(name = "features")
public class Features extends TimestampEntity implements GrantedAuthority {

    private String authority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}