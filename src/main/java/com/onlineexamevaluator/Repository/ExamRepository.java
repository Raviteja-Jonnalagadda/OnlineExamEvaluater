package com.onlineexamevaluator.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ExamRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean doesExamIdExist(String examId) {
        String sql = "SELECT COUNT(*) FROM exams WHERE exam_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, examId);
        return count != null && count > 0;
    }
}
