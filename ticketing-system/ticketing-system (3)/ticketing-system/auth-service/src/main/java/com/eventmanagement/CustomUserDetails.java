package com.eventmanagement;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return roles as authorities (you can enhance this logic to support roles or permissions)
        return null;  // or return a list with roles
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // You can implement account expiry logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // You can implement account locking logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // You can implement credentials expiry logic
    }

    @Override
    public boolean isEnabled() {
        return true;  // You can implement account enabling logic
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
