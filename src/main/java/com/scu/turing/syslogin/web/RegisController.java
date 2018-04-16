package com.scu.turing.syslogin.web;

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

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/user")
public class RegisController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFilter.class);

    @Autowired
    private UserService service;

    @GetMapping(value = "/register")
    public Object register(){
        return "register";
    }

    @PostMapping(value = "/register")
    public Object register(Model model,
                           @RequestParam String email,
                           @RequestParam String pwd) {
        User user = new User();
        user.setEmail(email);
        user.setPwd(pwd);
        service.saveUser(user);
        return "login";
    }

    @GetMapping(value = "/login")
    public Object login(){
        return "login";
    }

    @PostMapping(value = "/login")
    public Object login(HttpServletRequest request,
                        @RequestParam String email,
                        @RequestParam String pwd) {
        User user = service.findByEmail(email);
        if (user == null){
            LOGGER.warn("User not exits!Please regist first and then login.");
            return "register";
        }
        if (!user.getPwd().equals(pwd)){
            LOGGER.warn("Password error!");
        }

        request.getSession().setAttribute(UserFilter.SESSION_USER_KEY, user);

        return "home";
    }
}
