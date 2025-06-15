package com.onlineexamevaluator.Repository;


import com.onlineexamevaluator.model.StudentExamModel;
import java.util.List;

public interface StudentExamRepository {
    List<StudentExamModel> getQuestionsByExamId(int examId);
}
