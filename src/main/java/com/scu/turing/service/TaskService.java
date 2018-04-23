package com.scu.turing.service;

import com.scu.turing.model.Comment;
import com.scu.turing.model.Task;
import com.scu.turing.repository.CommentRepository;
import com.scu.turing.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Value("${base.task.finishCount}")
    private int finishCount;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CommentRepository commentRepository;

    public Task getTaskById(long id) {
        return taskRepository.getOne(id);
    }

    public Task getTaskByTaskName(String taskName) {
        return taskRepository.findByTaskName(taskName);
    }

    public Task save(Task t) {
        return taskRepository.save(t);
    }

    public Slice<Task> requireAllRecentTasks(int page, int size) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "id");
        return taskRepository.findAll(pageable);
    }

    public double getProgressInPercentage(Long taskId) {
        int finished = commentRepository.countByTaskId(taskId);
        return finished / finishCount;
    }
}
