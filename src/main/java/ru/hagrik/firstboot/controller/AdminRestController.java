package ru.hagrik.firstboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hagrik.firstboot.config.SpringSecurityConfig;
import ru.hagrik.firstboot.model.Role;
import ru.hagrik.firstboot.model.User;
import ru.hagrik.firstboot.service.RoleService;
import ru.hagrik.firstboot.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    final RoleService roleService;
    final UserService userService;
    final SpringSecurityConfig springSecurityConfig;
    final ObjectMapper objectMapper;

    public AdminRestController(RoleService roleService, UserService userService, SpringSecurityConfig springSecurityConfig, ObjectMapper objectMapper) {
        this.roleService = roleService;
        this.userService = userService;
        this.springSecurityConfig = springSecurityConfig;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/allUsers")
    @ResponseBody
    public ResponseEntity<String> userList() {
        try {
            List<User> userList = userService.findAllUsers();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonList = objectMapper.writeValueAsString(userList);
            return new ResponseEntity<>(jsonList, HttpStatus.OK);
        } catch (NoSuchElementException | JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @PostMapping(value = "/saveUser")
    @ResponseBody
    public ResponseEntity<User> saveUser(@RequestBody String user) {
        return getUserResponseEntity(user);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<User> removeUser(@PathVariable("id") Long id) {
        try {
            userService.removeUserById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<User> edit(@PathVariable("id") User userFromDb, @RequestBody String user) {
        return getUserResponseEntity(user);
    }

    private ResponseEntity<User> getUserResponseEntity(@RequestBody String user) throws JsonProcessingException {
        try {
            User userToEdit = objectMapper.readValue(user, User.class);
            List<Role> allRoles = roleService.findAllRoles();
            Set<Role> roleSet = new HashSet<>();
            for (Role role : allRoles) {
                if (user.contains(role.toString())) {
                    roleSet.add(role);
                }
            }
            userToEdit.setRoles(roleSet);
            userToEdit.setPassword(springSecurityConfig.passwordEncoder().encode(userToEdit.getPassword()));
            userService.saveUser(userToEdit);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<User> findOne(@PathVariable("id") Long id) {
        try {
            User user = userService.findUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
