package com.onlineexamevaluator.Repository;

public interface StudentEvaluateRepository {
    String getCorrectAnswerForQuestion(int questionId);
    int getTotalMarksForExam(String examId);
    int getPassMarksForExam(String examId);
    void saveExamResult(String examId, String studentId, int obtainedMarks, String resultStatus);
}