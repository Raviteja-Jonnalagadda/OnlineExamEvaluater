package com.onlineexamevaluator.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlineexamevaluator.model.AddQuestionExamDTO;
import com.onlineexamevaluator.model.EditExamData;
import com.onlineexamevaluator.model.EditExamFinalExam;
import com.onlineexamevaluator.services.AddQuestionExamService;
import com.onlineexamevaluator.services.AuthService;
import com.onlineexamevaluator.services.EditExamFinalService;
import com.onlineexamevaluator.services.EditExamService;
import com.onlineexamevaluator.services.ExamService;

@Controller
public class AdminController {

	private JdbcTemplate jdbcTemplate;

	// Handle GET request to show the role selection page
	@GetMapping("/")
	public String showRoleSelectionPage() {
		return "redirect:/html/role-selection.html";
	}

	// Handle POST request for role selection
	@PostMapping("/role-handler")
	public String handleRoleSelection(@RequestParam String role) {
		if ("admin".equalsIgnoreCase(role)) {
			return "redirect:/html/admin_login.html";
		} else if ("student".equalsIgnoreCase(role)) {
			return "redirect:/html/student/student_login.html";
		} else {
			return "redirect:/html/role-selection.html";
		}
	}

	@PostMapping("/admin_login")
	public String adminloginhandelerc(@RequestParam("username") String uname, @RequestParam("password") String psword) {

		System.out.println(uname + "  " + psword);
		return "";
	}

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
		String uname = credentials.get("uname");
		String pword = credentials.get("pword");

		boolean isValid = authService.authenticate(uname, pword);

		if (isValid) {
			return ResponseEntity.ok("success");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("fail");
		}
	}

	@PostMapping("/checkExamId")
	public Map<String, Object> checkExamId(@RequestParam("exam_id") Long examId) {
		String sql = "SELECT COUNT(*) FROM exams WHERE exam_id = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, examId);
		Map<String, Object> response = new HashMap<>();
		if (count > 0) {
			response.put("exists", true); // Exam ID already exists
		} else {
			response.put("exists", false); // Exam ID is available
		}

		return response;
	}

	@Autowired
	private ExamService examService;

	@GetMapping("/checkExamId/{examId}")
	public ResponseEntity<String> checkExamId(@PathVariable String examId) {
		boolean exists = examService.checkExamIdExists(examId);
		return ResponseEntity.ok(exists ? "exists" : "available");
	}

	@Autowired
	private AddQuestionExamService addquestexamService;

	@PostMapping("/submitExam")
	public ResponseEntity<Map<String, String>> submitExam(@RequestBody AddQuestionExamDTO exam) {
		System.out.println(exam + "  exam data");
		System.out.println(exam + "  exam data");
		Map<String, String> response = new HashMap<>();

		if (addquestexamService.saveExam(exam)) {
			response.put("status", "success");
			response.put("message", "Exam saved successfully");
			return ResponseEntity.ok(response);
		} else {
			response.put("status", "error");
			response.put("message", "Failed to save exam");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Autowired
	private DataSource dataSource;

	@GetMapping("/getAllExams")
	@ResponseBody
	public List<Map<String, Object>> getAllExams() {
		List<Map<String, Object>> exams = new ArrayList<>();
		String sql = "SELECT exam_id, exam_name FROM exams";

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Map<String, Object> exam = new HashMap<>();
				exam.put("examId", rs.getInt("exam_id"));
				exam.put("examName", rs.getString("exam_name"));
				exams.add(exam);
			}

			rs.close();
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return exams;
	}

	@Autowired
	private EditExamService editExamService;

	@GetMapping("/getExamDetails")
	@ResponseBody
	public EditExamData getExamDetails(@RequestParam int examId) {
		System.out.println("[Get Exam Details For the Exam ID :  --->  [ " + examId + " ]");
		return editExamService.getExamById(examId);
	}

	@Autowired
	private EditExamFinalService editExamFinalService;

	@PostMapping("/updateExam")
	@ResponseBody

	public String updateExam(@RequestBody EditExamFinalExam examData) {
		System.out.println("[Recived data for exam Update is  Exam Data: " + examData.toString()); // Assuming you have
																									// a toString()
																									// method in your
																									// object class

		// Debug: Log each field if needed
		System.out.println("[Recived data for exam Update is ] Exam ID: " + examData.getExamId());
		System.out.println("[Recived data for exam Update is ] Exam Name: " + examData.getExamName());
		System.out.println("[Recived data for exam Update is ] No of Questions: " + examData.getNoOfQuestions());
		System.out.println("[Recived data for exam Update is ] Duration: " + examData.getTimeDuration()); // Check if
																											// it's null
		System.out.println("[Recived data for exam Update is ] Total Marks: " + examData.getTotalMarks());
		System.out.println("[Recived data for exam Update is ] Pass Marks: " + examData.getPassMarks());
		boolean isUpdated = editExamFinalService.updateExam(examData);
		if (isUpdated) {
			return "Exam updated successfully!";
		} else {
			return "Failed to update exam!";
		}
	}

	@GetMapping("/getExamDetailsForDelete")
	@ResponseBody
	public Map<String, Object> getExamDetailsForDelete(@RequestParam int examId) {
		Map<String, Object> response = new HashMap<>();

		try (Connection conn = dataSource.getConnection()) {
			// Fetch exam details
			String examSql = "SELECT exam_id, exam_name, NO_OF_QUESTIONS, time_duration, total_marks, pass_marks, created_by "
					+ "FROM exams WHERE exam_id = ?";
			PreparedStatement examPs = conn.prepareStatement(examSql);
			examPs.setInt(1, examId);
			ResultSet examRs = examPs.executeQuery();

			if (examRs.next()) {
				Map<String, Object> exam = new HashMap<>();
				exam.put("examId", examRs.getInt("exam_id"));
				exam.put("examName", examRs.getString("exam_name"));
				exam.put("totalQuestions", examRs.getInt("NO_OF_QUESTIONS"));
				exam.put("duration", examRs.getInt("time_duration"));
				exam.put("totalMarks", examRs.getInt("total_marks"));
				exam.put("passMark", examRs.getInt("pass_marks"));
				exam.put("makerId", examRs.getString("created_by"));
				response.put("exam", exam);
			}

			examRs.close();
			examPs.close();

			// Fetch exam questions
			String questionSql = "SELECT question, option1, option2, option3, option4, correct_option "
					+ "FROM exam_questions WHERE exam_id = ?";
			PreparedStatement qps = conn.prepareStatement(questionSql);
			qps.setInt(1, examId);
			ResultSet qrs = qps.executeQuery();

			List<Map<String, String>> questions = new ArrayList<>();
			while (qrs.next()) {
				Map<String, String> q = new HashMap<>();
				q.put("question", qrs.getString("question"));
				q.put("options", String.join(", ", qrs.getString("option1"), qrs.getString("option2"),
						qrs.getString("option3"), qrs.getString("option4")));
				q.put("correctOption", qrs.getString("correct_option"));
				questions.add(q);
			}

			response.put("questions", questions);

			qrs.close();
			qps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	@DeleteMapping("/deleteExam")
	@ResponseBody
	public Map<String, String> deleteExam(@RequestParam int examId) {
		Map<String, String> response = new HashMap<>();

		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false); // Start transaction

			// Step 1: Delete questions
			String deleteQuestions = "DELETE FROM exam_questions WHERE exam_id = ?";
			ps1 = conn.prepareStatement(deleteQuestions);
			ps1.setInt(1, examId);
			ps1.executeUpdate();

			// Step 2: Delete exam
			String deleteExam = "DELETE FROM exams WHERE exam_id = ?";
			ps2 = conn.prepareStatement(deleteExam);
			ps2.setInt(1, examId);
			ps2.executeUpdate();

			conn.commit(); // Commit transaction
			response.put("status", "success");
			response.put("message", "Exam and its questions deleted successfully.");

		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback(); // Rollback if any error
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			response.put("status", "error");
			response.put("message", "Failed to delete exam.");
		} finally {
			try {
				if (ps1 != null)
					ps1.close();
				if (ps2 != null)
					ps2.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return response;
	}

}
