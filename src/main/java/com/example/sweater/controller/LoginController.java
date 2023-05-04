package com.example.sweater.controller;

import com.example.sweater.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {

    @ModelAttribute
    public User getUser(){
        return new User();
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
