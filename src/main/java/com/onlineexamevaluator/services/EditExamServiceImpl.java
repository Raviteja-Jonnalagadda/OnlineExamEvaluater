package com.onlineexamevaluator.services;


import com.onlineexamevaluator.model.EditExamData;
import com.onlineexamevaluator.model.EditExamQuestion;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EditExamServiceImpl implements EditExamService {

    private final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private final String USER = "system";
    private final String PASSWORD = "system";

    public EditExamData getExamById(int examId) {
        EditExamData exam = new EditExamData();
        List<EditExamQuestion> questions = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {

            // 1. Fetch exam details
        	String examQuery = "SELECT * FROM exams WHERE exam_id = ?";
        	try (PreparedStatement ps = conn.prepareStatement(examQuery)) {
        	    ps.setInt(1, examId);
        	    try (ResultSet rs = ps.executeQuery()) {
        	        if (rs.next()) {
        	            exam.setExamId(rs.getInt("EXAM_ID"));
        	            exam.setExamName(rs.getString("EXAM_NAME"));
        	            exam.setNoOfQuestions(rs.getInt("NO_OF_QUESTIONS"));
        	            exam.setDuration(rs.getInt("TIME_DURATION"));
        	            exam.setTotalMarks(rs.getInt("TOTAL_MARKS"));
        	            exam.setPassMarks(rs.getInt("PASS_MARKS"));
        	            exam.setMakerId(rs.getInt("CREATED_BY"));
        	            // If your exam object has a createdDate field, you can add:
        	            // exam.setCreatedDate(rs.getDate("CREATED_DATE"));
        	        }
        	    }
        	}

            // 2. Fetch exam questions
            String questionQuery = "SELECT * FROM exam_questions WHERE exam_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(questionQuery)) {
                ps.setInt(1, examId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        EditExamQuestion question = new EditExamQuestion();
                        question.setQuestionId(rs.getInt("question_id"));
                        question.setQuestionText(rs.getString("question"));
                        question.setOption1(rs.getString("option1"));
                        question.setOption2(rs.getString("option2"));
                        question.setOption3(rs.getString("option3"));
                        question.setOption4(rs.getString("option4"));
                        question.setCorrectOption(rs.getString("correct_option"));
                        questions.add(question);
                    }
                }
            }
            exam.setQuestions(questions);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exam;
    }
}
