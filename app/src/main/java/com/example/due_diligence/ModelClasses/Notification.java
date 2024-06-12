package com.example.due_diligence.ModelClasses;

public class Notification {
    private String message;
    private Boolean isRead;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Notification(String message, Boolean isRead) {
        this.message = message;
        this.isRead = isRead;
    }
}
