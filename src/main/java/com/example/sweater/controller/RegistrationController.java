package com.example.sweater.controller;

import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User vmUser,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", vmUser);
            return "registration";
        }
        if (vmUser.getConfirmPassword() == null ||
            vmUser.getPassword() == null
            || !vmUser.getConfirmPassword().equals(vmUser.getPassword())) {

            model.addAttribute("passwordErrorMatch", "Passwords don't match");
            model.addAttribute("user", vmUser);
            return "registration";
        }
        if (!userService.addUser(vmUser)) {
            model.addAttribute("message", "User exists BOIIIIIIII!");
            model.addAttribute("user", vmUser);
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) model.addAttribute("message", "User successfully activated");
        else model.addAttribute("message", "Activate code is not found!!");
        return "login";
    }
}
