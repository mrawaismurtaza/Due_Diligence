package com.example.due_diligence.ModelClasses;

public class Task {
    private String name;
    private String Status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Task(String name, String status) {
        this.name = name;
        Status = status;
    }
}
