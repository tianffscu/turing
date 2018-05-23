package com.scu.turing.service.repository;

import com.scu.turing.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTaskName(String taskName);

    Page<Task> findByIdIn(List<Long> ids,Pageable pageable);

    Page<Task> findByFinishedFalseAndIdNotIn(List<Long> ids, Pageable pageable);

    Slice<Task> findByOwnerIdOrderByIdDesc(long ownerId, Pageable pageable);

//    @Query("select count(t) from Task t where ")
//    long countAllTaskInProgress();

    long countAllByFinishedTrue();
}
