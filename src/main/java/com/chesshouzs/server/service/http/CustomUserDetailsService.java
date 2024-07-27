package com.chesshouzs.server.service.http;

import com.chesshouzs.server.model.Users;
import com.chesshouzs.server.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UUID userId = UUID.fromString(id);
        Users user = usersRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}