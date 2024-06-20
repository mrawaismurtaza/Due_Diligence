package com.example.due_diligence.ModelClasses;

public class Submission {
    private String submissionId;
    private String projectId;
    private String submissionURL;
    private String detail;

    public Submission(String submissionId, String projectId, String submissionURL, String detail) {
        this.submissionId = submissionId;
        this.projectId = projectId;
        this.submissionURL = submissionURL;
        this.detail = detail;
    }


    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSubmissionURL() {
        return submissionURL;
    }

    public void setSubmissionURL(String submissionURL) {
        this.submissionURL = submissionURL;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
