package com.onlineexamevaluator.services;

import com.onlineexamevaluator.model.AnswerSubmission;
import com.onlineexamevaluator.model.ResultResponse;

public interface StudentEvaluateService {
    ResultResponse evaluateAndStore(AnswerSubmission submission);

	int getTotalMarksForExam(String examId);
}
