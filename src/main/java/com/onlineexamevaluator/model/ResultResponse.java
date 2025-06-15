package com.onlineexamevaluator.model;

public class ResultResponse {
    private int obtainedMarks;
    private int totalMarks;
    private String resultStatus;

    public ResultResponse(int obtainedMarks, int totalMarks, String resultStatus) {
        this.obtainedMarks = obtainedMarks;
        this.totalMarks = totalMarks;
        this.resultStatus = resultStatus;
    }

    public int getObtainedMarks() {
        return obtainedMarks;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public String getResultStatus() {
        return resultStatus;
    }
}
