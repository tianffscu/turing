package com.scu.turing.repository;

import com.scu.turing.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment,Long> {

//    Slice<Comment> findByTask(Task TaskId, Pageable pageable);
    Slice<Comment> findByTaskId(long TaskId, Pageable pageable);

    int countByTaskId(long taskId);
}
