package com.scu.turing.entity;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "comment_task_id", columnList = "task_id"),
        @Index(name = "comment_content", columnList = "content")})
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 2550)
    private String content;

    @ManyToOne(targetEntity = Task.class, fetch = FetchType.LAZY)
    private Task task;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
