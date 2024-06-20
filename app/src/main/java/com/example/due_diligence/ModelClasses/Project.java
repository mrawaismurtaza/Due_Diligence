package com.example.due_diligence.ModelClasses;

import java.io.Serializable;
import java.util.List;

public class Project implements Serializable {

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    private String studentId;
    private String name;
    private String description;
    private String supervisor;
    private List<Task> tasks;
    private String id;
    private String proposalUrl;
    private int submissionCount; // Use primitive int

    public int getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(int submissionCount) {
        this.submissionCount = submissionCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProposalUrl() {
        return proposalUrl;
    }

    public void setProposalUrl(String proposalUrl) {
        this.proposalUrl = proposalUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Project(String studentId, String name, String description, String supervisor, List<Task> tasks, String id, String proposalUrl, int submissionCount) {
        this.studentId = studentId;
        this.name = name;
        this.description = description;
        this.supervisor = supervisor;
        this.tasks = tasks;
        this.id = id;
        this.proposalUrl = proposalUrl;
        this.submissionCount = submissionCount;
    }
}
