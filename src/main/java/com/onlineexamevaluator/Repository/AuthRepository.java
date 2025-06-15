package com.onlineexamevaluator.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkCredentials(String uname, String pword) {
        String sql = "SELECT COUNT(*) FROM online_exam_users WHERE username = ? AND password = ? AND role = 'admin'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, uname, pword);
        return count != null && count == 1;
    }
}
