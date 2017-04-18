package com.todo.todo.models;


public class subTask_item {

    private String key, status, title;

    public subTask_item() {
    }

    public subTask_item(String key, String status, String title) {
        this.key = key;
        this.status = status;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
