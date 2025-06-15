package com.onlineexamevaluator.model;

import java.util.Date;
import java.util.List;

public class EditExamFinalExam {

    private Long examId;
    private String examName;
    private Integer noOfQuestions;
    private Integer timeDuration;
    private Integer totalMarks;
    private Integer passMarks;
    private Long createdBy;
    private Date createdDate;
    private List<EditExamFinalQuestion> questions;

    
    // Getters and Setters
    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Integer getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(Integer noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public Integer getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Integer timeDuration) {
        this.timeDuration = timeDuration;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Integer getPassMarks() {
        return passMarks;
    }

    public void setPassMarks(Integer passMarks) {
        this.passMarks = passMarks;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public void setQuestions(List<EditExamFinalQuestion> questions) {
        this.questions = questions;
    }
    public List<EditExamFinalQuestion> getQuestions() {
        return questions;
    }


    @Override
    public String toString() {
        return "EditExamFinalExam{" +
                "examId=" + examId +
                ", examName='" + examName + '\'' +
                ", noOfQuestions=" + noOfQuestions +
                ", timeDuration=" + timeDuration +
                ", totalMarks=" + totalMarks +
                ", passMarks=" + passMarks +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                '}';
    }
}
