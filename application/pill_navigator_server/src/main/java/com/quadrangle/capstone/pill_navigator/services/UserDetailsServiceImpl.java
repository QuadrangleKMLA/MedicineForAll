package com.quadrangle.capstone.pill_navigator.services;

import com.quadrangle.capstone.pill_navigator.entities.User;
import com.quadrangle.capstone.pill_navigator.entities.UserDetailsImpl;
import com.quadrangle.capstone.pill_navigator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found."));

        return UserDetailsImpl.build(user);
    }
}
