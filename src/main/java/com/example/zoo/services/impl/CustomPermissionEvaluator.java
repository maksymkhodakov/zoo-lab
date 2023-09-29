package com.example.zoo.services.impl;

import com.example.zoo.entity.User;
import com.example.zoo.enums.Privilege;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (permission instanceof Privilege) {
            return hasPrivilege((Privilege) permission, (User) authentication.getPrincipal());
        }
        return false;
    }

    private boolean hasPrivilege(Privilege permission, User authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(p -> p.getPrivilege().equals(permission));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}
