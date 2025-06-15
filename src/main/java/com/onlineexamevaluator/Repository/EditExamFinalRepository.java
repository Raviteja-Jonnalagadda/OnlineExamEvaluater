package com.onlineexamevaluator.Repository;

import com.onlineexamevaluator.model.EditExamFinalExam;
import com.onlineexamevaluator.model.EditExamFinalQuestion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EditExamFinalRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Fetch the exam details by examId from the 'exams' table
    @SuppressWarnings("deprecation")
	public EditExamFinalExam findExamById(Long examId) {
        String sql = "SELECT * FROM exams WHERE exam_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{examId}, (rs, rowNum) -> {
            EditExamFinalExam exam = new EditExamFinalExam();
            exam.setExamId(rs.getLong("exam_id"));
            exam.setExamName(rs.getString("exam_name"));
            exam.setNoOfQuestions(rs.getInt("no_of_questions"));
            exam.setTimeDuration(rs.getInt("time_duration"));
            exam.setTotalMarks(rs.getInt("total_marks"));
            exam.setPassMarks(rs.getInt("pass_marks"));
            exam.setCreatedBy(rs.getLong("created_by"));
            exam.setCreatedDate(rs.getDate("created_date"));
            return exam;
        });
    }

    // Fetch the list of questions for a specific examId from the 'exam_questions' table
    @SuppressWarnings("deprecation")
	public List<EditExamFinalQuestion> findQuestionsByExamId(Long examId) {
        String sql = "SELECT * FROM exam_questions WHERE exam_id = ?";
        return jdbcTemplate.query(sql, new Object[]{examId}, (rs, rowNum) -> {
            EditExamFinalQuestion question = new EditExamFinalQuestion();
            question.setQuestionId(rs.getLong("question_id"));
            question.setExamId(rs.getLong("exam_id"));
            question.setQuestionText(rs.getString("question"));
            question.setOption1(rs.getString("option1"));
            question.setOption2(rs.getString("option2"));
            question.setOption3(rs.getString("option3"));
            question.setOption4(rs.getString("option4"));
            question.setCorrectOption(rs.getString("correct_option"));
            return question;
        });
    }

    // Update exam details in the 'exams' table
    public boolean updateExam(EditExamFinalExam examData) {
        String sql = "UPDATE exams SET exam_name = ?, no_of_questions = ?, time_duration = ?, total_marks = ?, pass_marks = ?, created_by = ?, created_date = ? WHERE exam_id = ?";
        int result = jdbcTemplate.update(sql, examData.getExamName(), examData.getNoOfQuestions(), examData.getTimeDuration(),
                examData.getTotalMarks(), examData.getPassMarks(), examData.getCreatedBy(), examData.getCreatedDate(), examData.getExamId());
        return result > 0;
    }

    // Update question details in the 'exam_questions' table
    public boolean updateQuestion(EditExamFinalQuestion question) {
        String sql = "UPDATE exam_questions SET question = ?, option1 = ?, option2 = ?, option3 = ?, option4 = ?, correct_option = ? WHERE question_id = ?";
        int result = jdbcTemplate.update(sql, question.getQuestionText(), question.getOption1(), question.getOption2(),
                question.getOption3(), question.getOption4(), question.getCorrectOption(), question.getQuestionId());
        return result > 0;
    }
}
