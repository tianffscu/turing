package com.scu.turing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Evaluation {

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private long taskId;
    private double evaluation;
    private Date time;

    public Evaluation() {
    }

    public Evaluation(long taskId, double evaluation, Date time) {
        this.taskId = taskId;
        this.evaluation = evaluation;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
