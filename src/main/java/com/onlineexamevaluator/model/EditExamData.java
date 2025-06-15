package com.onlineexamevaluator.model;


import java.util.List;

public class EditExamData {
    private int examId;
    private String examName;
    private int noOfQuestions;
    private int duration;
    private int totalMarks;
    private int passMarks;
    private int makerId;

    private List<EditExamQuestion> questions;

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public int getNoOfQuestions() {
		return noOfQuestions;
	}

	public void setNoOfQuestions(int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}

	public int getPassMarks() {
		return passMarks;
	}

	public void setPassMarks(int passMarks) {
		this.passMarks = passMarks;
	}

	public int getMakerId() {
		return makerId;
	}

	public void setMakerId(int makerId) {
		this.makerId = makerId;
	}

	public List<EditExamQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<EditExamQuestion> questions) {
		this.questions = questions;
	}

}
