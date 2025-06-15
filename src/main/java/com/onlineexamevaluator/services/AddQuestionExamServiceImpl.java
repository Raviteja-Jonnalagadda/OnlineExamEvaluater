package com.onlineexamevaluator.services;

import com.onlineexamevaluator.Repository.AddQuestionExamRepository;
import com.onlineexamevaluator.model.AddQuestionExamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddQuestionExamServiceImpl implements AddQuestionExamService {

    @Autowired
    private AddQuestionExamRepository examRepository;

    @Override
    public boolean saveExam(AddQuestionExamDTO exam) {
        return examRepository.insertExamAndQuestions(exam);
    }
}