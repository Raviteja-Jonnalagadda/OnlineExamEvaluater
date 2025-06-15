package com.onlineexamevaluator.services;


import com.onlineexamevaluator.model.EditExamData;

public interface EditExamService {
    EditExamData getExamById(int examId);
}
