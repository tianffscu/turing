package com.scu.turing.service;

import com.scu.turing.model.Comment;
import com.scu.turing.repository.CommentRepository;
import com.scu.turing.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Slice<Comment> requireAllRecentComments(long taskId, int page, int size) {
        return commentRepository.findByTaskId(taskId, new PageRequest(page, size));
    }
}
