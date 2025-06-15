package com.onlineexamevaluator.services;

import com.onlineexamevaluator.Repository.StudentLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentLoginService {

    @Autowired
    private StudentLoginRepository loginRepo;

    public boolean authenticate(String username, String password) {
        return loginRepo.validateCredentials(username, password);
    }
}
