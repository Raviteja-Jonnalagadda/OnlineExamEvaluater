package com.onlineexamevaluator.Repository;

import com.onlineexamevaluator.model.AddQuestionExamDTO;

public interface AddQuestionExamRepository {
    boolean insertExamAndQuestions(AddQuestionExamDTO exam);
}
