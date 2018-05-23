package com.scu.turing.web.controller;

import com.google.common.base.Strings;
import com.scu.turing.comm.Const;
import com.scu.turing.entity.*;
import com.scu.turing.model.ExceptionMsg;
import com.scu.turing.model.Response;
import com.scu.turing.model.ResponseData;
import com.scu.turing.model.ServerException;
import com.scu.turing.model.facade.CommentFacade;
import com.scu.turing.model.facade.TaskFacade;
import com.scu.turing.service.CommentService;
import com.scu.turing.service.RateService;
import com.scu.turing.service.TaskService;
import com.scu.turing.service.UserService;
import com.scu.turing.utils.FileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class TaskController extends BaseController {

    private TaskService taskService;
    private UserService userService;
    private RateService rateService;
    private CommentService commentService;

    @Value("${web.audio.path}")
    private String audioFilePath;

    public TaskController(@Autowired TaskService taskService,
                          @Autowired UserService userService,
                          @Autowired CommentService commentService,
                          @Autowired RateService rateService) {
        this.taskService = taskService;
        this.userService = userService;
        this.commentService = commentService;
        this.rateService = rateService;
    }

    @PostMapping("/task")
    public Response createTask(@RequestParam("taskName") String taskName,
                               @RequestParam("description") String description,
                               @RequestParam("ownerId") long ownerId,
                               @RequestParam("finishHit") int hit,
                               @RequestParam(name = "ownerDefTxt", required = false) String ownerDefTxt,
                               @RequestParam("audioUri") String audioUri) {
        try {
            User user = userService.getUserById(ownerId);
            if (!Role.getAdmin().equals(user.getRole())) {
                throw new ServerException(ExceptionMsg.NO_PERMISSION);
            }
            Task task = new Task(ownerId, taskName, description, hit, ownerDefTxt);

            task.setAudioUri("/" + audioUri);
            task = taskService.saveTask(task);
            return result(task.getId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @PostMapping("/audio")
    public Response uploadAudio(@RequestParam("file") MultipartFile audioFile) {
        try {
            String fileName = FileUtil.getLegalFileName(UUID.randomUUID() + audioFile.getOriginalFilename()) + ".mp3";
            FileUtil.uploadFile(audioFile.getBytes(), audioFilePath, fileName);
            return result(fileName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 更新任务详情
     */
    @PostMapping("/task/u0")
    public Response updateTask(@RequestParam("id") long taskId,
                               @RequestParam("userId") long userId,
                               @RequestParam(name = "taskName", required = false) String taskName,
                               @RequestParam(name = "description", required = false) String description) {
        try {
            User user = userService.getUserById(userId);
            Task task = taskService.getTaskById(taskId);

            if (!Role.getAdmin().equals(user.getRole())
                    || task.getOwnerId() != userId) {
                throw new ServerException(ExceptionMsg.NO_PERMISSION);
            }
            if (!Strings.isNullOrEmpty(taskName.trim())) {
                task.setTaskName(taskName.trim());
            }
            if (!Strings.isNullOrEmpty(description.trim())) {
                task.setDescription(description.trim());
            }

            taskService.saveTask(task);
            return success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @GetMapping("/task")
    public Response requireAllTask(@RequestParam("userId") long userId,
                                   @RequestParam("page") int page,
                                   @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        try {
            long taskCount = taskService.countAllTaskInProgress();
            Page<Task> tasks = taskService.requireAllRecentTasksNonInvolvedByUserId(userId, page, size);
            return result(new Object[]{tasks.getTotalElements(), tasks.getContent().stream()
                    .map(this::task2Facade)
                    .collect(Collectors.toList())});
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @GetMapping("/task/{id}")
    public Response requireOneTask(@PathVariable("id") long taskId,
                                   @RequestParam("userId") long userId) {
        try {
            Task task = taskService.getTaskById(taskId);
            int currentFinishCount = taskService.getCurrentFinishCount(taskId);
            TaskFacade facade = task2Facade(task);
            facade.setCurrentParticipant(currentFinishCount);

            if (userId == task.getOwnerId()) {
                List<Comment> comments = taskService.requireAllRecentComment(taskId, 0, Const.DEFAULT_PAGE_SIZE);
                List<CommentFacade> commentFacades = comments.stream().map(this::comment2Facade).collect(Collectors.toList());

                List<RateHistory> rateHistories = rateService.findByTaskId(taskId);
                if (!CollectionUtils.isEmpty(rateHistories)) {
                    Map<Long, RateHistory> userId2Rate = rateHistories.stream()
                            .collect(Collectors.toMap(RateHistory::getUserId, Function.identity()));

                    commentFacades.forEach(c -> {
                        RateHistory history;
                        if ((history = userId2Rate.get(c.getUserId())) != null) {
                            c.setRate(history.getRate());
                        }
                    });
                }
                facade.setComments(commentFacades);
            } else {
                Comment comment = commentService.requireCommentByTaskIdAndUserId(taskId, userId);
                facade.setComments(Collections.singletonList(comment2Facade(comment)));
            }

            return result(facade);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @GetMapping("/history/{id}/task")
    public Response requireAllHistoryTasks(@PathVariable("id") long userId,
                                           @RequestParam("page") int page,
                                           @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        try {
            Page<Task> tasks = taskService.requireAllRecentTasksByParticipant(userId, page, size);
            return result(new Object[]{tasks.getTotalElements(), tasks.getContent().stream().
                    map(this::task2Facade)
                    .collect(Collectors.toList())});
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @PostMapping("/task/{taskId}/comment")
    public Response createComment2Task(@PathVariable("taskId") long taskId,
                                       @RequestParam("content") String content,
                                       @RequestParam("userId") long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                throw new ServerException(ExceptionMsg.LoginNameNotExists);
            }

            Comment comment = new Comment(content, taskId, userId, user.getUserName());
            taskService.saveComment(comment);
            return success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @GetMapping("/task/{id}/comment")
    public Response requireAllCommentOnTask(@PathVariable("id") long taskId,
                                            @RequestParam("page") int page,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        try {
            List<Comment> comments = taskService.requireAllRecentComment(taskId, page, size);
            return result(comments.stream()
                    .map(this::comment2Facade)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    private TaskFacade task2Facade(Task task) {
        TaskFacade facade = new TaskFacade();
        BeanUtils.copyProperties(task, facade);
        return facade;
    }

    private CommentFacade comment2Facade(Comment comment) {
        CommentFacade facade = new CommentFacade();
        BeanUtils.copyProperties(comment, facade);
        return facade;
    }

//    @PostMapping("/task/add")
//    public Response addTask(@RequestParam String taskName,
//                            @RequestParam String description,
//                            @RequestParam String ownerName) {
//        try {
//            //todo: check Params
//            if (!getUser().getRole().equals(Role.getAdmin())) {
//                throw new ServerException(ExceptionMsg.NO_PERMISSION);
//            }
//
//            User owner = userService.getByUserName(ownerName);
//            Objects.requireNonNull(owner, "UserFacade not exists!");
//
//            Task task = new Task();
//            task.setTaskName(taskName);
//            task.setDescription(description);
//            task.setOwnerId(owner.getId());
//            taskService.saveTask(task);
//            return result();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            if (e instanceof ServerException) {
//                return failed(((ServerException) e).getExpMsg());
//            }
//            return simpleFailed();
//        }
//    }

//    @PostMapping("/task/progress")
//    public Response getTaskProgress(@RequestParam Long taskId) {
//        try {
//            //todo: check Params
//            //????????????????//
//            double progress = taskService.getProgressInPercentage(taskId);
//            return new ResponseData(ExceptionMsg.SUCCESS, progress);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return simpleFailed();
//        }
//    }
//
//    @PostMapping("/audiofiles/add")
//    public Response addAudio(@RequestParam Long taskId,
//                             @RequestParam MultipartFile audioFile,
//                             HttpServletRequest request) {
//        try {
//            //todo: check Params
//            //????????????????//
//            String fileName = FileUtil.getLegalFileName(
//                    new String(Base64.encode(audioFile.getOriginalFilename().getBytes())));
//
//            Task task = taskService.getTaskById(taskId);
//            Objects.requireNonNull(task, "Task not exist!");
//
//            String filePath = request.getSession().getServletContext().getRealPath("audios/");
//            FileUtil.uploadFile(audioFile.getBytes(), filePath, fileName);
//
//            task.setAudioUri("/audios/" + fileName);
////            TODO: 2018/4/23 异步执行自动识别和存库
////            AutoTranService.startTrans(taskId,fileName);
//            taskService.saveTask(task);
//            return result();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return simpleFailed();
//        }
//    }

//
//    @RequestMapping("/comment/add")
//    public Response addComment(@RequestParam String userName,
//                               @RequestParam Long taskId,
//                               @RequestParam String commentContent) {
//        try {
//            User user = userService.getByUserName(userName);
//            Objects.requireNonNull(user);
//
//            Task task = taskService.getTaskById(taskId);
//            Objects.requireNonNull(task);
//
//            Comment comment = new Comment();
//            comment.setUser(user);
//            comment.setTask(task);
//            comment.setContent(commentContent);
//            commentService.save(comment);
//
//            return result();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return simpleFailed();
//        }
//    }
//
//    @RequestMapping("/comment/recent")
//    public Response getRecentComment(@RequestParam Long taskId) {
//        try {
//            Slice<Comment> comments = commentService.requireAllRecentComments(taskId, 0, 5);
//            return new ResponseData(ExceptionMsg.SUCCESS, comments);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return simpleFailed();
//        }
//    }
}
