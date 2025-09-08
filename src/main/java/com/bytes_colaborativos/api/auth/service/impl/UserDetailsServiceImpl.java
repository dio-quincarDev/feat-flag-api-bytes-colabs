package com.bytes_colaborativos.api.auth.service.impl;

import com.bytes_colaborativos.api.auth.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserEntityRepository userEntityRepository;

    public UserDetailsServiceImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User with email" + username + "not found"));
    }
}

