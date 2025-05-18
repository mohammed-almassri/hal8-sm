package me.hal8.sm.auth.service.impl;

import lombok.AllArgsConstructor;
import me.hal8.sm.auth.entity.CustomUserDetails;
import me.hal8.sm.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@AllArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username).orElseThrow(()->{
            throw new UsernameNotFoundException("no user with email "+username);
        });
        return new CustomUserDetails(user);
    }
}
