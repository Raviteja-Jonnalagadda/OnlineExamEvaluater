package com.onlineexamevaluator.model;

import java.util.List;

public class AddQuestionExamDTO {
 public int examId;
 public String examName;
 public int noOfQuestions;
 public int duration;
 public int totalMarks;
 public int passMarks;
 public String makerId;
 public List<AddQuestionDTO> questions;
}
