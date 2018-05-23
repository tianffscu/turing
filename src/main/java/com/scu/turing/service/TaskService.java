package com.scu.turing.service;

import com.google.common.collect.Lists;
import com.scu.turing.entity.Comment;
import com.scu.turing.entity.Task;
import com.scu.turing.service.repository.CommentRepository;
import com.scu.turing.service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private CommentRepository commentRepository;

    public TaskService(@Autowired TaskRepository taskRepository,
                       @Autowired CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
    }

    public Task getTaskById(long id) {
        return taskRepository.getOne(id);
    }

    public Task getTaskByTaskName(String taskName) {
        return taskRepository.findByTaskName(taskName);
    }

    public Task saveTask(Task t) {
        return taskRepository.save(t);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public long countAllTaskInProgress() {
        return taskRepository.countAllByFinishedTrue();
    }

    public int getCurrentFinishCount(long taskId) {
        return commentRepository.countByTaskId(taskId);
    }

    public List<Comment> requireAllRecentComment(long taskId, int page, int size) {
        Pageable req = new PageRequest(page, size);
        return Lists.newArrayList(commentRepository.findByTaskIdOrderByIdDesc(taskId, req));
    }

    public List<Comment> requireAllCommentByUserId(long userId) {
        return commentRepository.findByUserIdOrderByIdDesc(userId);
    }

    public Page<Task> requireAllRecentTasksNonInvolvedByUserId(long userId, int page, int size) {
        List<Comment> comments = commentRepository.findByUserIdOrderByIdDesc(userId);
        Pageable pageReq = new PageRequest(page, size);
        return taskRepository.findByFinishedFalseAndIdNotIn(comments.stream().
                map(Comment::getTaskId).
                collect(Collectors.toList()), pageReq);
    }

    public List<Task> requireAllRecentTasks(int page, int size) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "id");
        return Lists.newArrayList(taskRepository.findAll(pageable));
    }

    public Page<Task> requireAllRecentTasksByParticipant(long userId, int page, int size) {
        Pageable req = new PageRequest(page, size);
        Slice<Comment> comments = commentRepository.findByUserIdOrderByIdDesc(userId, req);
        return taskRepository.findByIdIn(Lists.newArrayList(comments).stream()
                .map(Comment::getTaskId).collect(Collectors.toList()), req);
    }

    public List<Task> requireAllRecentTasksByOwner(long userId, int page, int size) {
        Pageable req = new PageRequest(page, size);
        return Lists.newArrayList(taskRepository.findByOwnerIdOrderByIdDesc(userId, req));
    }
}
