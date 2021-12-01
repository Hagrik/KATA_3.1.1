package ru.hagrik.firstboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    final SpringSecurityConfig springSecurityConfig;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, SpringSecurityConfig springSecurityConfig) {
        this.userService = userService;
        this.roleService = roleService;
        this.springSecurityConfig = springSecurityConfig;
    }

    @GetMapping
    public String userList(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "adminPage";
    }

    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roleSet", roleService.findAllRoles());
        return "addUserPage";
    }

    @PostMapping("/addUser")
    public String saveUser(User user) {
        user.setPassword(springSecurityConfig.passwordEncoder().encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/removeUser/{id}")
    public String removeUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/editUserForm/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roleSet", roleService.findAllRoles());
        return "editUserPage";
    }

    @PatchMapping("/editUser")
    public String editUser(User user) {
        user.setPassword(springSecurityConfig.passwordEncoder().encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/userInfoForm/{id}")
    public String userInfoForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "userInfoPage";
    }

}
