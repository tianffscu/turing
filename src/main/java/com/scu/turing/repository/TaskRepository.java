package com.scu.turing.repository;

import com.scu.turing.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long>{

    Task findByTaskName(String taskName);

}
