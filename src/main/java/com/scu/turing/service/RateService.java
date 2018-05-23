package com.scu.turing.service;

import com.scu.turing.entity.RateHistory;
import com.scu.turing.service.repository.RateHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateService {

    private RateHistoryRepository rateHistoryRepository;

    public RateService(@Autowired RateHistoryRepository rateHistoryRepository) {
        this.rateHistoryRepository = rateHistoryRepository;
    }

    public List<RateHistory> findByTaskId(long taskId) {
        return rateHistoryRepository.findByTaskId(taskId);
    }

    public List<RateHistory> findByUserId(long userId) {


        return null;
    }

    public RateHistory findByTaskIdAndUserId(long taskId, long userId) {


        return null;
    }
}
