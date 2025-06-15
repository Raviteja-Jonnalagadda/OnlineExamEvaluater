package com.onlineexamevaluator.Repository;

import com.onlineexamevaluator.model.AddQuestionDTO;
import com.onlineexamevaluator.model.AddQuestionExamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

@Repository
public class AddQuestionExamRepositoryImpl implements AddQuestionExamRepository {

    @Autowired
    private DataSource dataSource;

    @Override
    public boolean insertExamAndQuestions(AddQuestionExamDTO exam) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. First insert the exam
            String insertExamSQL = "INSERT INTO exams (exam_id, exam_name, no_of_questions, " +
                                 "time_duration, total_marks, pass_marks, created_by, created_date) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE)";
            
            PreparedStatement psExam = conn.prepareStatement(insertExamSQL);
            psExam.setInt(1, exam.examId);
            psExam.setString(2, exam.examName);
            psExam.setInt(3, exam.noOfQuestions);
            psExam.setInt(4, exam.duration);
            psExam.setInt(5, exam.totalMarks);
            psExam.setInt(6, exam.passMarks);
            
            // Handle created_by (makerId) which might be null or invalid
            if (exam.makerId != null && !exam.makerId.isEmpty()) {
                try {
                    psExam.setInt(7, Integer.parseInt(exam.makerId));
                } catch (NumberFormatException e) {
                    psExam.setNull(7, Types.INTEGER);
                }
            } else {
                psExam.setNull(7, Types.INTEGER);
            }
            
            int examRows = psExam.executeUpdate();
            if (examRows != 1) {
                throw new RuntimeException("Exam insertion failed");
            }

            // 2. Now insert questions (only if exam was inserted successfully)
            String insertQuestionSQL = "INSERT INTO exam_questions (question_id, exam_id, question, " +
                                      "option1, option2, option3, option4, correct_option) " +
                                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement psQ = conn.prepareStatement(insertQuestionSQL);
            for (AddQuestionDTO q : exam.questions) {
                try {
                    int questionId = Integer.parseInt(q.questionId);
                    
                    // Validate we have exactly 4 options
                    if (q.options == null || q.options.size() != 4) {
                        throw new RuntimeException("Question must have exactly 4 options");
                    }
                    
                    psQ.setInt(1, questionId);
                    psQ.setInt(2, exam.examId); // Same exam_id used here
                    psQ.setString(3, q.questionText);
                    psQ.setString(4, q.options.get(0));
                    psQ.setString(5, q.options.get(1));
                    psQ.setString(6, q.options.get(2));
                    psQ.setString(7, q.options.get(3));
                    psQ.setString(8, q.correctOption != null ? q.correctOption : q.answer);
                    psQ.addBatch();
                } catch (Exception e) {
                    conn.rollback();
                    throw new RuntimeException("Error processing question: " + q.questionId, e);
                }
            }
            psQ.executeBatch();
            
            conn.commit();
            return true;
            
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) {}
            }
            throw new RuntimeException("Failed to insert exam and questions", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
    }
}