package com.scu.turing.web.controller;

import com.scu.turing.entity.RateHistory;
import com.scu.turing.entity.Task;
import com.scu.turing.entity.UserRate;
import com.scu.turing.model.ExceptionMsg;
import com.scu.turing.model.Response;
import com.scu.turing.model.ResponseData;
import com.scu.turing.model.ServerException;
import com.scu.turing.model.facade.RateFacade;
import com.scu.turing.service.RateService;
import com.scu.turing.service.TaskService;
import com.scu.turing.service.repository.UserRateRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class RateController extends BaseController {

    private RateService rateService;
    private UserRateRepository userRateRepository;
    private TaskService taskService;

    public RateController(@Autowired RateService rateService,
                          @Autowired UserRateRepository userRateRepository,
                          @Autowired TaskService taskService) {
        this.rateService = rateService;
        this.userRateRepository = userRateRepository;
        this.taskService = taskService;
    }

    @PostMapping("/task/{taskId}/rate")
    public Response updateRate(@PathVariable("taskId") long taskId,
                               @RequestParam("rate") int rate,
                               @RequestParam("userId") long userId,
                               @RequestParam("rateUser") long rateUser) {

        try {
            RateHistory rateHistory = new RateHistory(rateUser, taskId, rate, new Date());
            rateHistory = rateService.save(rateHistory);

            List<RateHistory> rateHistories = rateService.findByUserId(rateUser);
            UserRate userRate = userRateRepository.findByUserId(rateUser);
            if (userRate == null) {
                userRate = new UserRate(rateUser, 0);
            }
            userRate.setRate(rateHistories.stream().mapToInt(RateHistory::getRate).summaryStatistics().getAverage());
            userRate.setTime(new Date());
            userRateRepository.save(userRate);
            return success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @GetMapping("/rate")
    public Response getRate(@RequestParam("userId") long userId) {

        try {
            UserRate userRate = userRateRepository.findByUserId(userId);
            List<RateHistory> rateHistories = rateService.findByUserId(userId);

            if (CollectionUtils.isEmpty(rateHistories)) {
                return result(new Object[]{userRate.getRate(), Collections.emptyList()});
            }

            List<Task> tasks = taskService.getTaskByIds(rateHistories.stream()
                    .map(RateHistory::getTaskId)
                    .collect(Collectors.toList()));
            Map<Long, Task> taskId2Task = tasks.stream()
                    .collect(Collectors.toMap(Task::getId, Function.identity()));

            List<RateFacade> rateFacades = rateHistories.stream()
                    .map(this::rate2RateFacade)
                    .peek(r -> {
                        if (taskId2Task.get(r.getTaskId()) != null) {
                            r.setTaskName(taskId2Task.get(r.getTaskId()).getTaskName());
                        }
                    })
                    .collect(Collectors.toList());
            return result(new Object[]{userRate.getRate(), rateFacades});
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    private RateFacade rate2RateFacade(RateHistory rateHistory) {
        RateFacade facade = new RateFacade();
        BeanUtils.copyProperties(rateHistory, facade);
        return facade;
    }
}
