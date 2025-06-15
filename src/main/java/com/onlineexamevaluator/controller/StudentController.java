package com.onlineexamevaluator.controller;

import com.onlineexamevaluator.model.AnswerSubmission;
import com.onlineexamevaluator.model.ResultResponse;
import com.onlineexamevaluator.model.StudentExamModel;
import com.onlineexamevaluator.services.StudentEvaluateService;
import com.onlineexamevaluator.services.StudentExamService;
import com.onlineexamevaluator.services.StudentLoginService;
import com.onlineexamevaluator.util.EmailSender;
import com.onlineexamevaluator.util.ResultImageGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

@RestController
public class StudentController {

    @Autowired
    private StudentLoginService loginService;

    @PostMapping("/studentlogin")
    public String studentLogin(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        boolean isValid = loginService.authenticate(username, password);
        return isValid ? "success" : "fail";
    }
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/exams")
    public List<Map<String, Object>> getAllExams() {
        String sql = "SELECT exam_id, exam_name FROM exams";

        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> exam = new HashMap<String, Object>();
                exam.put("examId", rs.getInt("exam_id"));
                exam.put("examName", rs.getString("exam_name"));
                return exam;
            }
        });
    }
    

        @Autowired
        private StudentExamService studentExamService;

        @GetMapping("/questions")
        public List<StudentExamModel> getExamQuestions(@RequestParam int examId) {
            return studentExamService.fetchQuestions(examId);
        }
        
        @SuppressWarnings("deprecation")
		@GetMapping("/examdetails")
        public Map<String, Object> getExamDetails(@RequestParam int examId) {
            String sql = "SELECT exam_name, time_duration, no_of_questions FROM exams WHERE exam_id = ?";

            return jdbcTemplate.queryForObject(sql, new Object[]{examId}, (rs, rowNum) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("examName", rs.getString("exam_name"));
                map.put("timeDuration", rs.getInt("time_duration"));
                map.put("noOfQuestions", rs.getInt("no_of_questions"));
                return map;
            });
        }

        @Autowired
        private StudentEvaluateService studentEvaluateService;

        @PostMapping("/submit")
        public ResponseEntity<ResultResponse> submitExam(@RequestBody AnswerSubmission submission) {
            ResultResponse result = studentEvaluateService.evaluateAndStore(submission);
            return ResponseEntity.ok(result);
        }
        
        @GetMapping("/studentResults")
        public List<Map<String, Object>> getStudentResults(@RequestParam String studentId) {
            System.out.println("Fetching results for studentId: " + studentId);

            String sql = "SELECT EXAM_ID, RESULT_ID, OBTAINED_MARKS, RESULT_STATUS, TO_CHAR(RESULT_DATE, 'DD-MON-YY') AS RESULT_DATE FROM exam_results WHERE student_id = ? ORDER BY RESULT_DATE DESC";

            @SuppressWarnings("deprecation")
			List<Map<String, Object>> results = jdbcTemplate.query(sql, new Object[]{studentId}, (ResultSet rs, int rowNum) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("examId", rs.getString("EXAM_ID"));
                map.put("obtainedMarks", rs.getInt("OBTAINED_MARKS"));
                map.put("resultStatus", rs.getString("RESULT_STATUS"));
                map.put("resultDate", rs.getString("RESULT_DATE"));
                map.put("RESULT_ID", rs.getString("RESULT_ID"));
                return map;
            });

            System.out.println("Results found: " + results.size());
            return results;
}
        @GetMapping("/downloadresults")
        public void downloadResultImage(@RequestParam String type,
                                         @RequestParam(required = false) String studentId,
                                         @RequestParam(required = false) String resultId,
                                         HttpServletResponse response) {
            try {
                if ("full".equalsIgnoreCase(type)) {
                    List<Map<String, Object>> allResults = jdbcTemplate.queryForList(
                            "SELECT * FROM exam_results WHERE student_id = ?", studentId);
                    ResultImageGenerator.generateFullResultImage(allResults, response);
                } else if ("onerec".equalsIgnoreCase(type)) {
                    Map<String, Object> oneResult = jdbcTemplate.queryForMap(
                            "SELECT * FROM exam_results WHERE result_id = ?", Integer.valueOf(resultId));
                    ResultImageGenerator.generateSingleResultImage(oneResult, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        @GetMapping("/getProfile")
        public ResponseEntity<Map<String, Object>> getProfile(@RequestParam String studentId) {
            try {
                Map<String, Object> profile = jdbcTemplate.queryForMap(
                    "SELECT * FROM user_profile WHERE student_id = ?", studentId);
                return ResponseEntity.ok(profile);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        
        private static final String ADMIN_EMAIL = "raviteja032766@example.com"; // admin email here

        @PostMapping("/contactAdmin")
        public ResponseEntity<?> contactAdmin(@RequestBody Map<String, String> payload) {
            String studentId = payload.get("studentId");
            String type = payload.get("type");       // feedback or report
            String message = payload.get("message");

            if (studentId == null || type == null || message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Missing required fields or message is empty.");
            }

            String subject = "Student " + studentId + " - " + (type.equalsIgnoreCase("feedback") ? "Feedback" : "Report Issue");
            String body = "Student ID: " + studentId + "\nType: " + type + "\n\nMessage:\n" + message;

            boolean mailSent = EmailSender.sendEmail(ADMIN_EMAIL, subject, body);

            if (mailSent) {
                return ResponseEntity.ok("Your " + type + " has been sent successfully.");
            } else {
                return ResponseEntity.status(500).body("Failed to send email. Please try again later.");
            }
        }
}
