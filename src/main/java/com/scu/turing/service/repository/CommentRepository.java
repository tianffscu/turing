package com.scu.turing.service.repository;

import com.scu.turing.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    //    Slice<Comment> findByTask(Task TaskId, Pageable pageable);
    Slice<Comment> findByTaskIdOrderByIdDesc(long taskId, Pageable pageable);

    int countByTaskId(long taskId);

    Comment findByTaskIdAndUserId(long taskId, long userId);

    Slice<Comment> findByUserIdOrderByIdDesc(long userId, Pageable pageable);

    List<Comment> findByUserIdOrderByIdDesc(long userId);
}
