package com.ajitapp.smartwork.models;

public class SubTaskModel {
    private String taskImage;
    private String taskName;
    private String taskId;
    private String taskDescription;

    private String subTaskImage;
    private String subTaskName;
    private String subTaskId;
    private String subTaskDesc;

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskImage(String taskImage) {
        this.taskImage = taskImage;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setSubTaskImage(String subTaskImage) {
        this.subTaskImage = subTaskImage;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public void setSubTaskId(String subTaskId) {
        this.subTaskId = subTaskId;
    }

    public void setSubTaskDesc(String subTaskDesc) {
        this.subTaskDesc = subTaskDesc;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getSubTaskImage() {
        return subTaskImage;
    }
    public String getSubTaskDesc() {
        return subTaskDesc;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public String getSubTaskId() {
        return subTaskId;
    }
}