package com.scu.turing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long ownerId;
    @Column(unique = true)
    private String taskName;
    private String description;

    private String audioUri;

    private String autoTransTxt;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri = audioUri;
    }

    public String getAutoTransTxt() {
        return autoTransTxt;
    }

    public void setAutoTransTxt(String autoTransTxt) {
        this.autoTransTxt = autoTransTxt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", audioUri='" + audioUri + '\'' +
                ", autoTransTxt='" + autoTransTxt + '\'' +
                '}';
    }
}
