package com.avinash.springsecurityjwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Value("${app.username}")
    private String myUsername;

    @Value("${app.password}")
    private String myPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals("avinash")){
            throw new UsernameNotFoundException("Username not found");
        }
        return new User(myUsername, myPassword, new ArrayList<>());
    }
}
