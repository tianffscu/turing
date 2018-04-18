package com.scu.turing.crowdsourcing.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomePageController {

    @GetMapping("")
    public Object home() {
        return "home";
    }


}
