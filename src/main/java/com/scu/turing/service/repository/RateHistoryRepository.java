package com.scu.turing.service.repository;

import com.scu.turing.entity.RateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateHistoryRepository extends JpaRepository<RateHistory, Long> {

    List<RateHistory> findByTaskId(long taskId);

}
