package com.onlineexamevaluator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlineexamevaluator.Repository.ExamRepository;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Override
    public boolean checkExamIdExists(String examId) {
        return examRepository.doesExamIdExist(examId);
    }


}
