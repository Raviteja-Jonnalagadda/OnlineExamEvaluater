package com.onlineexamevaluator.model;

import java.util.List;

public class AddQuestionDTO {
    public String questionId;
    public String questionText;
    public List<String> options; // must be List<String>
    public String answer;
    public String marks;
	public String correctOption;

    // Add getters & setters or make fields public
}
