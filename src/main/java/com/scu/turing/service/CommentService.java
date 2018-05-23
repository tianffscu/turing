package com.scu.turing.service;

import com.scu.turing.entity.Comment;
import com.scu.turing.service.repository.CommentRepository;
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
        return commentRepository.findByTaskIdOrderByIdDesc(taskId, new PageRequest(page, size));
    }

    public Comment requireCommentByTaskIdAndUserId(long taskId, long userId) {
        return commentRepository.findByTaskIdAndUserId(taskId, userId);
    }
}
