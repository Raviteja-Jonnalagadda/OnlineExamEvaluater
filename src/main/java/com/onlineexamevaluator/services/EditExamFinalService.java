package com.onlineexamevaluator.services;

import com.onlineexamevaluator.model.EditExamFinalExam;
import com.onlineexamevaluator.model.EditExamFinalQuestion;
import com.onlineexamevaluator.Repository.EditExamFinalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditExamFinalService {

    @Autowired
    private EditExamFinalRepository editExamFinalRepository;

    // Fetch exam details by examId
    public EditExamFinalExam getExamDetails(String examId) {
        // Fetch exam details and questions from the repository
        EditExamFinalExam exam = editExamFinalRepository.findExamById(Long.parseLong(examId)); // Ensure ID is passed as Long or Integer
        List<EditExamFinalQuestion> questions = editExamFinalRepository.findQuestionsByExamId(Long.parseLong(examId));
        exam.setQuestions(questions); // Ensure that setQuestions method is present in EditExamFinalExam class
        return exam;
    }

    // Update the exam data and questions
    public boolean updateExam(EditExamFinalExam examData) {
        // Update exam details
        boolean isExamUpdated = editExamFinalRepository.updateExam(examData);

        // Update each question for the exam
        if (isExamUpdated) {
            for (EditExamFinalQuestion question : examData.getQuestions()) {
                editExamFinalRepository.updateQuestion(question);
            }
            return true;
        }
        return false;
    }
}
