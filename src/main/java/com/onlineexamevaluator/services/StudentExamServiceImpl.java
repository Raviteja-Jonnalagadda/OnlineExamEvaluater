package com.onlineexamevaluator.services;


import com.onlineexamevaluator.model.StudentExamModel;
import com.onlineexamevaluator.Repository.StudentExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentExamServiceImpl implements StudentExamService {

    @Autowired
    private StudentExamRepository studentExamRepository;

    @Override
    public List<StudentExamModel> fetchQuestions(int examId) {
        return studentExamRepository.getQuestionsByExamId(examId);
    }
}
