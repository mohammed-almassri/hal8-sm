package com.microtwitter.followservice.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {
    private String username;
    @Getter
    private UUID id;
    private String roles;


    public CustomUserDetails(UUID id, String email, String roles) {
        this.id = id;
        this.username = email;
        this.roles = roles;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(",")).map((role)->new SimpleGrantedAuthority("ROLE_"+role)).toList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
