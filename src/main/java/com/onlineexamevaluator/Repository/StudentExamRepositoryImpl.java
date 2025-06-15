package com.onlineexamevaluator.Repository;


import com.onlineexamevaluator.model.StudentExamModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentExamRepositoryImpl implements StudentExamRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("deprecation")
	@Override
    public List<StudentExamModel> getQuestionsByExamId(int examId) {
        String sql = "SELECT question_id, question, option1, option2, option3, option4 FROM exam_questions WHERE exam_id = ?";
        return jdbcTemplate.query(sql, new Object[]{examId}, new RowMapper<StudentExamModel>() {
            @Override
            public StudentExamModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                StudentExamModel question = new StudentExamModel();
                question.setQuestionId(rs.getInt("question_id"));
                question.setQuestion(rs.getString("question"));
                question.setOption1(rs.getString("option1"));
                question.setOption2(rs.getString("option2"));
                question.setOption3(rs.getString("option3"));
                question.setOption4(rs.getString("option4"));
                return question;
            }
        });
    }
}

