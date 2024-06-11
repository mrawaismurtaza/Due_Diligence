package com.example.due_diligence.ModelClasses;

public class Notification {
    private String message;
    private Boolean isRead;
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Notification(String message, Boolean isRead, String userId) {
        this.message = message;
        this.isRead = isRead;
        this.userId = userId;
    }
}
