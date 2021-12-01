package ru.hagrik.firstboot.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hagrik.firstboot.model.User;
import ru.hagrik.firstboot.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String userInfoForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "userInfoPage";
    }
}