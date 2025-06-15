package com.onlineexamevaluator.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Random;

import javax.sql.DataSource;

@Repository
public class StudentEvaluateRepositoryImpl implements StudentEvaluateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentEvaluateRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String getCorrectAnswerForQuestion(int questionId) {
        // Log method entry and parameter
        System.out.println("Entering getCorrectAnswerForQuestion with questionId = " + questionId);
        // Prepare SQL to fetch the correct answer for the given question ID
        String sql = "SELECT CORRECT_OPTION FROM exam_questions WHERE question_id = ?";
        System.out.println("SQL Query: " + sql);
        // Execute query; retrieve the correct answer as a String
        String correctAnswer = jdbcTemplate.queryForObject(sql, String.class, questionId);
        System.out.println("Correct answer for question " + questionId + ": " + correctAnswer);
        return correctAnswer;
    }

    public int getTotalMarksForExam(String examId) {
        // Log method entry and parameter
        System.out.println("Entering getTotalMarksForExam with examId = " + examId);
        // Prepare SQL to fetch total marks for the given exam ID
        String sql = "SELECT total_marks FROM exams WHERE exam_id = ?";
        System.out.println("SQL Query: " + sql);
        // Execute query; retrieve the total marks as an Integer
        int totalMarks = jdbcTemplate.queryForObject(sql, Integer.class, examId);
        System.out.println("Total marks for exam " + examId + ": " + totalMarks);
        return totalMarks;
    }

    public int getPassMarksForExam(String examId) {
        // Log method entry and parameter
        System.out.println("Entering getPassMarksForExam with examId = " + examId);
        // Prepare SQL to fetch passing marks for the given exam ID
        String sql = "SELECT pass_marks FROM exams WHERE exam_id = ?";
        System.out.println("SQL Query: " + sql);
        // Execute query; retrieve the passing marks as an Integer
        int passMarks = jdbcTemplate.queryForObject(sql, Integer.class, examId);
        System.out.println("Pass marks for exam " + examId + ": " + passMarks);
        return passMarks;
    }

    public void saveExamResult(String examId, String studentId, int obtainedMarks, String resultStatus) {
        // Log method entry and parameters
        System.out.println("Entering saveExamResult with examId = " + examId 
            + ", studentId = " + studentId 
            + ", obtainedMarks = " + obtainedMarks 
            + ", resultStatus = " + resultStatus);
        // Generate a random result_id for the exam_results table
        int resultId = new Random().nextInt(1000000);
        System.out.println("Generated random resultId: " + resultId);
        // Prepare SQL to insert a new exam result record
        String sql = "INSERT INTO exam_results (result_id, exam_id, student_id, obtained_marks, result_status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        System.out.println("SQL Query: " + sql);
        // Execute insert; jdbcTemplate.update returns the number of rows affected
        int rows = jdbcTemplate.update(sql, resultId, examId, studentId, obtainedMarks, resultStatus);
        System.out.println("Inserted rows into exam_results: " + rows);
    }


}
