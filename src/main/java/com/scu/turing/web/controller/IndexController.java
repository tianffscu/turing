package com.scu.turing.web.controller;

import com.scu.turing.comm.Const;
import com.scu.turing.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    @GetMapping("/")
    public String home(Model model) {
//        long size= collectRepository.countByUserIdAndIsDelete(getUserId(),IsDelete.NO);
//        Config config = configRepository.findByUserId(getUserId());
//        Favorites favorites = favoritesRepository.findOne(Long.parseLong(config.getDefaultFavorties()));
//        List<String> followList = followRepository.findByUserId(getUserId());
//        model.addAttribute("config",config);
//        model.addAttribute("favorites",favorites);
//        model.addAttribute("size",size);
//        model.addAttribute("followList",followList);
        model.addAttribute("user",getUser());
//        model.addAttribute("newAtMeCount",noticeRepository.countByUserIdAndTypeAndReaded(getUserId(), "at", "unread"));
//        model.addAttribute("newCommentMeCount",noticeRepository.countByUserIdAndTypeAndReaded(getUserId(), "comment", "unread"));
//        model.addAttribute("newPraiseMeCount",noticeRepository.countPraiseByUserIdAndReaded(getUserId(), "unread"));
//        logger.info("collect size="+size+" userID="+getUserId());
        return "home";
    }

    @GetMapping("/register")
    public String regist() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, Model model) {
        getSession().removeAttribute(Const.LOGIN_SESSION_KEY);
        getSession().removeAttribute(Const.LAST_REFERER);
        Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "/index";
    }

    @GetMapping("/authorize")
    public String authorize(){
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

    @GetMapping("/task/{taskId}")
    public String getTaskDetail(@PathVariable("taskId") Long taskId) {

        // FIXME: 2018/4/23

        return null;
    }
}
