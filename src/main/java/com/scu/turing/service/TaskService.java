package com.scu.turing.service;

import com.google.common.collect.Lists;
import com.scu.turing.entity.Comment;
import com.scu.turing.entity.Task;
import com.scu.turing.model.ExceptionMsg;
import com.scu.turing.model.ServerException;
import com.scu.turing.service.repository.CommentRepository;
import com.scu.turing.service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Task> getTaskByIds(List<Long> ids) {
        return taskRepository.findByIdIn(ids);
    }

    public Task getTaskByTaskName(String taskName) {
        return taskRepository.findByTaskName(taskName);
    }

    public Task saveTask(Task t) {
        return taskRepository.save(t);
    }

    @Transactional
    public Comment saveComment(Comment comment) {
        Task task = getTaskById(comment.getTaskId());
        int currentCount = getCurrentFinishCount(comment.getTaskId());
        if (currentCount >= task.getFinishHit()) {
            throw new ServerException(ExceptionMsg.TASK_CLOSED);
        }
        if ((currentCount + 1) == task.getFinishHit()) {
            task.setFinished(true);
        }
        taskRepository.save(task);
        onTaskFinished();
        return commentRepository.save(comment);
    }

    private void onTaskFinished() {
        // TODO: 2018/5/23 调用语音识别接口和相似度匹配检测
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

    public List<Task> requireAllFinishedRecentTasksByOwner(long userId) {
        return Lists.newArrayList(taskRepository.findByOwnerIdAndFinishedTrueOrderByIdDesc(userId));
    }

    public List<Task> requireAllInProgressRecentTasksByOwner(long userId) {
        return Lists.newArrayList(taskRepository.findByOwnerIdAndFinishedFalseOrderByIdDesc(userId));
    }
}
