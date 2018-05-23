package com.scu.turing.service.repository;

import com.scu.turing.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Evaluation findByTaskId(long taskId);
}
