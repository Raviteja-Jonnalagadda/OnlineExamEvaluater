package com.onlineexamevaluator.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class StudentLoginRepository {

    @Autowired
    private DataSource dataSource;

    public boolean validateCredentials(String username, String password) {
        boolean result = false;
        String sql = "SELECT COUNT(*) FROM online_exam_users WHERE username = ? AND password = ? AND role = 'student'";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
