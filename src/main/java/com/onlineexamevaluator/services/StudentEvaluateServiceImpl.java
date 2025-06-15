package com.onlineexamevaluator.services;

import com.onlineexamevaluator.model.AnswerSubmission;
import com.onlineexamevaluator.model.ResultResponse;
import com.onlineexamevaluator.Repository.StudentEvaluateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StudentEvaluateServiceImpl implements StudentEvaluateService {

    @Autowired
    private StudentEvaluateRepository studentEvaluateRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ResultResponse evaluateAndStore(AnswerSubmission submission) {
        System.out.println("===== Evaluation Started =====");
        System.out.println("Received submission for Exam ID: " + submission.getExamId());
        System.out.println("Student ID: " + submission.getStudentId());
        System.out.println("Submitted Answers: " + submission.getAnswers());

        int correctAnswers = 0;
        Map<String, String> submittedAnswers = submission.getAnswers();
        int totalQuestions = (submittedAnswers != null) ? submittedAnswers.size() : 0;

        if (totalQuestions == 0) {
            System.out.println("No answers submitted. Returning 0 marks.");
            return new ResultResponse(0, 0, "N");
        }

        for (Map.Entry<String, String> entry : submittedAnswers.entrySet()) {
            try {
                int questionId = Integer.parseInt(entry.getKey());
                String selectedAnswer = entry.getValue();

                System.out.println("Evaluating Question ID: " + questionId + ", Submitted Answer: " + selectedAnswer);

                String correctAnswer = studentEvaluateRepository.getCorrectAnswerForQuestion(questionId);

                System.out.println("Correct Answer from DB: " + correctAnswer);

                if (correctAnswer != null && selectedAnswer != null &&
                        selectedAnswer.trim().equalsIgnoreCase(correctAnswer.trim())) {
                    correctAnswers++;
                    System.out.println("Answer is CORRECT. Total correct so far: " + correctAnswers);
                } else {
                    System.out.println("Answer is WRONG.");
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid question ID format: " + entry.getKey());
            } catch (Exception e) {
                System.err.println("Error evaluating question ID " + entry.getKey() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Total Correct Answers: " + correctAnswers);

        int totalMarks = studentEvaluateRepository.getTotalMarksForExam(submission.getExamId());
        int passMarks = studentEvaluateRepository.getPassMarksForExam(submission.getExamId());

        System.out.println("Total Marks for Exam: " + totalMarks);
        System.out.println("Pass Marks for Exam: " + passMarks);

        double marksPerQuestion = (totalQuestions > 0) ? (double) totalMarks / totalQuestions : 0.0;
        int obtainedMarks = (int) Math.round(correctAnswers * marksPerQuestion);

        System.out.println("Marks per Question: " + marksPerQuestion);
        System.out.println("Obtained Marks: " + obtainedMarks);

        String resultStatus = (obtainedMarks >= passMarks) ? "Y" : "N";
        System.out.println("Result Status (Y=Pass/N=Fail): " + resultStatus);

        studentEvaluateRepository.saveExamResult(
            submission.getExamId(),
            submission.getStudentId(),
            obtainedMarks,
            resultStatus
        );

        System.out.println("===== Evaluation Complete =====");

        return new ResultResponse(obtainedMarks, totalMarks, resultStatus);
    }

    @Override
    public int getTotalMarksForExam(String examId) {
        System.out.println("Fetching total marks using JdbcTemplate for Exam ID: " + examId);
        String sql = "SELECT SUM(mark) FROM exam_questions WHERE exam_id = ?";
        Integer totalMarks = jdbcTemplate.queryForObject(sql, new Object[]{examId}, Integer.class);
        System.out.println("Total Marks fetched: " + totalMarks);
        return (totalMarks != null) ? totalMarks : 0;
    }
}
