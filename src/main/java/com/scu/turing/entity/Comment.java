package com.scu.turing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
//@Table(indexes = {@Index(name = "comment_task_id", columnList = "task_id")})
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 2550)
    private String content;

    //    @ManyToOne(targetEntity = Task.class, fetch = FetchType.LAZY)
    private long taskId;
    //    @ManyToOne(targetEntity = UserFacade.class, fetch = FetchType.LAZY)
    private long userId;
    @Deprecated
    private String userName;

    public Comment() {
    }

    public Comment(String content, long taskId, long userId, String userName) {
        this.content = content;
        this.taskId = taskId;
        this.userId = userId;
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
