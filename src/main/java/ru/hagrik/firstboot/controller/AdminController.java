package ru.hagrik.firstboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.hagrik.firstboot.config.SpringSecurityConfig;
import ru.hagrik.firstboot.model.User;
import ru.hagrik.firstboot.service.RoleService;
import ru.hagrik.firstboot.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    final UserService userService;
    final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String userList(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("user", user);
        model.addAttribute("roleSet", roleService.findAllRoles());
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "adminPage";
    }

    @GetMapping("/userInfoForm")
    public String userInfoForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        return "userInfoPage";
    }

}
