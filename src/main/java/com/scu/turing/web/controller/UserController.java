package com.scu.turing.web.controller;

import com.scu.turing.entity.User;
import com.scu.turing.model.ExceptionMsg;
import com.scu.turing.model.Response;
import com.scu.turing.model.ResponseData;
import com.scu.turing.model.ServerException;
import com.scu.turing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public Response register(@RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam("username") String userName) {
        try {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                throw new ServerException(ExceptionMsg.EmailUsed);
            }
            user = new User();
            user.setEmail(email);
            user.setPassword(getPwd(password));
            userService.saveUser(user);
            return success();
        } catch (Exception e) {
            logger.error("login simpleFailed, ", e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @PostMapping(value = "/login")
    public Response login(@RequestParam("email") String email,
                          @RequestParam("password") String password) {
        try {
            User loginUser = userService.getUserByEmail(email);
            if (loginUser == null) {
                throw new ServerException(ExceptionMsg.LoginNameNotExists);
            } else if (!loginUser.getPassword().equals(getPwd(password))) {
                throw new ServerException(ExceptionMsg.LoginNameOrPassWordError);
            }
            return success();
        } catch (Exception e) {
            logger.error("login simpleFailed, ", e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }
}
