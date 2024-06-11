package com.example.due_diligence.ModelClasses;

import java.util.List;

public class Project {
    private String name;
    private String description;
    private String supervisor;
    private List<Task> tasks;

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

    public Project(String name, String description, String supervisor, List<Task> tasks) {
        this.name = name;
        this.description = description;
        this.supervisor = supervisor;
        this.tasks = tasks;
    }
}
