package com.scu.turing.web.controller;

import com.scu.turing.entity.Role;
import com.scu.turing.entity.User;
import com.scu.turing.model.ExceptionMsg;
import com.scu.turing.model.Response;
import com.scu.turing.model.ResponseData;
import com.scu.turing.model.ServerException;
import com.scu.turing.model.facade.UserFacade;
import com.scu.turing.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
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
            logger.error(e.getMessage(), e);
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
            return result(user2Facade(loginUser));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @PostMapping("/password/{userId}")
    public Response updatePassword(@PathVariable("userId") long userId,
                                   @RequestParam("oldpass") String oldPassword,
                                   @RequestParam("newpass") String password) {
        try {
            User user = userService.getUserById(userId);
            if (!user.getPassword().equals(oldPassword)) {
                throw new ServerException(ExceptionMsg.LoginNameOrPassWordError);
            }
            user.setPassword(password);
            userService.saveUser(user);
            return success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }


    @GetMapping("/user/{id}")
    public Response userDetail(@PathVariable("id") long userId) {
        try {
            User user = userService.getUserById(userId);
            return result(user2Facade(user));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @PostMapping("/user/u0/{userId}")
    public Response updateUser(@PathVariable("userId") long userId,
                               @RequestParam("description") String desc) {
        try {
            User user = userService.getUserById(userId);
            user.setDescription(desc);
            userService.saveUser(user);
            return success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @PostMapping("/admin/{userId}")
    public Response becomeAdmin(@PathVariable("userId") long userId) {
        try {
            User user = userService.getUserById(userId);
            user.setRole(Role.getAdmin());
            userService.saveUser(user);
            return success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e instanceof ServerException) {
                return new ResponseData(((ServerException) e).getExpMsg());
            }
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    public UserFacade user2Facade(User user) {
        UserFacade facade = new UserFacade();
        BeanUtils.copyProperties(user, facade);
        return facade;
    }

}
