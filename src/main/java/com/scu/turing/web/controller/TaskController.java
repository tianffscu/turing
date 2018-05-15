package com.scu.turing.web.controller;

import com.scu.turing.entity.Comment;
import com.scu.turing.entity.Task;
import com.scu.turing.entity.User;
import com.scu.turing.entity.result.ExceptionMsg;
import com.scu.turing.entity.result.Response;
import com.scu.turing.entity.result.ResponseData;
import com.scu.turing.service.CommentService;
import com.scu.turing.service.TaskService;
import com.scu.turing.service.UserService;
import com.scu.turing.utils.Base64;
import com.scu.turing.utils.FileUtil;
import com.scu.turing.utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
public class TaskController extends BaseController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/task/all")
    public Response recentAll(@RequestParam int page,
                              @RequestParam int size) {
        try {
            ParamUtils.requirePositiveNumber(page, size);
            Slice<Task> tasks = taskService.requireAllRecentTasks(page, size);
//            tasks.
            return new ResponseData(ExceptionMsg.SUCCESS, tasks);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @PostMapping("/task/add")
    public Response addTask(@RequestParam String taskName,
                            @RequestParam String description,
                            @RequestParam String ownerName) {
        try {
            //todo: check Params
            //????????????????//

            User owner = userService.getByUserName(ownerName);
            Objects.requireNonNull(owner, "User not exists!");

            Task task = new Task();
            task.setTaskName(taskName);
            task.setDescription(description);
            task.setOwnerId(owner.getId());
            taskService.save(task);
            return result();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return failed();
        }
    }

    @PostMapping("/task/progress")
    public Response getTaskProgress(@RequestParam Long taskId) {
        try {
            //todo: check Params
            //????????????????//
            double progress = taskService.getProgressInPercentage(taskId);
            return new ResponseData(ExceptionMsg.SUCCESS, progress);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return failed();
        }
    }

    @PostMapping("/audio/add")
    public Response addAudio(@RequestParam Long taskId,
                             @RequestParam MultipartFile audioFile,
                             HttpServletRequest request) {
        try {
            //todo: check Params
            //????????????????//
            String fileName = FileUtil.getLegalFileName(
                    new String(Base64.encode(audioFile.getOriginalFilename().getBytes())));

            Task task = taskService.getTaskById(taskId);
            Objects.requireNonNull(task, "Task not exist!");

            String filePath = request.getSession().getServletContext().getRealPath("audios/");
            FileUtil.uploadFile(audioFile.getBytes(), filePath, fileName);

            task.setAudioUri("/audios/" + fileName);
//            TODO: 2018/4/23 异步执行自动识别和存库
//            AutoTranService.startTrans(taskId,fileName);
            taskService.save(task);
            return result();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return failed();
        }
    }


    @RequestMapping("/comment/add")
    public Response addComment(@RequestParam String userName,
                               @RequestParam Long taskId,
                               @RequestParam String commentContent) {
        try {
            User user = userService.getByUserName(userName);
            Objects.requireNonNull(user);

            Task task = taskService.getTaskById(taskId);
            Objects.requireNonNull(task);

            Comment comment = new Comment();
            comment.setUser(user);
            comment.setTask(task);
            comment.setContent(commentContent);
            commentService.save(comment);

            return result();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return failed();
        }
    }

    @RequestMapping("/comment/recent")
    public Response getRecentComment(@RequestParam Long taskId) {
        try {
            Slice<Comment> comments = commentService.requireAllRecentComments(taskId, 0, 5);
            return new ResponseData(ExceptionMsg.SUCCESS, comments);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return failed();
        }
    }

}
