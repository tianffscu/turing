package com.scu.turing.web.controller;

import com.scu.turing.comm.Const;
import com.scu.turing.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
//@RequestMapping("/")
public class IndexController extends BaseController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("user", getUser());
        logger.info("user: " + getUser());
        return "home";
    }

    @GetMapping("/register")
    public String regist() {
        return "register";
    }

    @GetMapping("/me")
    public String me(Model model) {
        model.addAttribute("user", getUser());
        return "user";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response,
                         HttpServletRequest request) {
        getSession().invalidate();
        Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect("/index");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @GetMapping("/authorize")
    public String authorize() {
        return "authorize";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("collector", "");
        User user = getUser();
        if (null != user) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/task/add")
    public String pageAddTask(Model model) {
        model.addAttribute("user", getUser());
        return "addTask";
    }

    @GetMapping("/task/center")
    public String getAllTask(Model model) {
        model.addAttribute("user", getUser());
        return "taskCenter";
    }

    @GetMapping("/task/{taskId}")
    public String getTaskDetail(@PathVariable("taskId") Long taskId) {

        // FIXME: 2018/4/23

        return null;
    }
}
