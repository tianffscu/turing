package com.scu.turing.syslogin.web;

import com.scu.turing.syslogin.model.Role;
import com.scu.turing.syslogin.model.User;
import com.scu.turing.syslogin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/user")
public class RegisController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisController.class);

    @Autowired
    private UserService service;

    @GetMapping(value = "/register")
    public Object register() {
        return "register";
    }

    @PostMapping(value = "/register")
    public Object register(Model model,
                           @RequestParam String email,
                           @RequestParam String pwd) {
        // TODO: 2018/4/18 Check email and pwd
        User user = new User();
        user.setEmail(email);
        user.setPwd(pwd);
        user.setRole(Role.getSysUser());
        service.saveUser(user);
        model.addAttribute("email", email);
        return "login";
    }

    @GetMapping(value = "/login")
    public Object login() {
        return "login";
    }

    @PostMapping(value = "/login")
    public Object login(HttpServletRequest request,
                        Model model,
                        @RequestParam String email,
                        @RequestParam String pwd) {
        User user = service.findByEmail(email);
        if (user == null) {
            LOGGER.warn("An non exist user try to login: " + email);
            model.addAttribute("tip", "User not exists!");
            return "login";
        }

        if (!user.getPwd().equals(pwd)) {
            model.addAttribute("tip", "Wrong password!");
            return "login";
        }

        request.getSession().setAttribute(UserFilter.SESSION_USER_KEY, user);
        return "home";
    }
}
