package com.todo.todo.models;

import java.io.Serializable;


public class todo_item implements Serializable {

    private String title, description, key, status, subtaskKey;

    public todo_item(String title, String description, String key, String status, String subtaskKey) {
        this.title = title;
        this.description = description;
        this.key = key;
        this.status = status;
        this.subtaskKey = subtaskKey;
    }

    public todo_item() {

    }

    public String getSubtaskKey() {
        return subtaskKey;
    }

    public void setSubtaskKey(String subtaskKey) {
        this.subtaskKey = subtaskKey;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
