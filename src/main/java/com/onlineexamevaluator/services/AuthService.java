package com.onlineexamevaluator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlineexamevaluator.Repository.AuthRepository;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    public boolean authenticate(String uname, String pword) {
        return authRepository.checkCredentials(uname, pword);
    }
}
