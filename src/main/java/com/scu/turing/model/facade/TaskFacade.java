package com.scu.turing.model.facade;

import java.util.List;

public class TaskFacade {

    private long id;

    private String taskName;

    private String description;

    private String audioUri;

    private int finishHit;

    private int currentParticipant;

    private List<CommentFacade> comments;

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

    public int getCurrentParticipant() {
        return currentParticipant;
    }

    public void setCurrentParticipant(int currentParticipant) {
        this.currentParticipant = currentParticipant;
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

    public int getFinishHit() {
        return finishHit;
    }

    public void setFinishHit(int finishHit) {
        this.finishHit = finishHit;
    }

    public List<CommentFacade> getComments() {
        return comments;
    }

    public void setComments(List<CommentFacade> comments) {
        this.comments = comments;
    }
}
