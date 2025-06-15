package com.onlineexamevaluator.services;

import com.onlineexamevaluator.model.AddQuestionExamDTO;

public interface AddQuestionExamService {
    boolean saveExam(AddQuestionExamDTO exam);
}
