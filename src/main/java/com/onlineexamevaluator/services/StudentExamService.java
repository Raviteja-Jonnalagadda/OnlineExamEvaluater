package com.onlineexamevaluator.services;


import com.onlineexamevaluator.model.StudentExamModel;
import java.util.List;

public interface StudentExamService {
    List<StudentExamModel> fetchQuestions(int examId);
}
