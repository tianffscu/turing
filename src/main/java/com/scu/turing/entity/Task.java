package com.scu.turing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private long ownerId;
    private String taskName;
    private String description;

    private int finishHit;

    private boolean finished;

    private String audioUri;

    private String autoTransTxt;
    private String ownerDefTxt;

    public Task() {
    }

    public Task(long ownerId, String taskName, String description,
                int finishHit, String ownerDefTxt) {
        this.ownerId = ownerId;
        this.taskName = taskName;
        this.description = description;
        this.finishHit = finishHit;
        this.ownerDefTxt = ownerDefTxt;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getFinishHit() {
        return finishHit;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setFinishHit(int finishHit) {
        this.finishHit = finishHit;
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

    public String getOwnerDefTxt() {
        return ownerDefTxt;
    }

    public void setOwnerDefTxt(String ownerDefTxt) {
        this.ownerDefTxt = ownerDefTxt;
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
