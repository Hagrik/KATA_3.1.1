package ru.hagrik.firstboot.dbInitializer;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.hagrik.firstboot.config.SpringSecurityConfig;
import ru.hagrik.firstboot.model.Role;
import ru.hagrik.firstboot.model.User;
import ru.hagrik.firstboot.repository.UserRepository;
import ru.hagrik.firstboot.service.RoleService;
import ru.hagrik.firstboot.service.UserService;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserGenerator {

    final UserService userService;
    final RoleService roleService;
    final SpringSecurityConfig springSecurityConfig;
    final UserRepository userRepository;

    public UserGenerator(UserService userService, RoleService roleService, SpringSecurityConfig springSecurityConfig, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.springSecurityConfig = springSecurityConfig;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void userGenerator() throws FileNotFoundException {
        Set<Role> roleAdmin = new HashSet<>();
        Set<Role> roleUser = new HashSet<>();
        Role admin = new Role("ROLE_ADMIN");
        roleAdmin.add(admin);
        Role user = new Role("ROLE_USER");
        roleUser.add(user);

        roleService.saveRole(admin);
        roleService.saveRole(user);

        User hren = new User();
        hren.setActive(true);
        hren.setName("Hren");
        hren.setLastname("Izosimov");
        hren.setEmail("hren@gmail.com");
        hren.setAge(33);
        hren.setPassword(springSecurityConfig.passwordEncoder().encode("100"));
        hren.setRoles(roleAdmin);

        User alsu = new User();
        alsu.setActive(true);
        alsu.setName("Alsu");
        alsu.setLastname("Shaiheeva");
        alsu.setEmail("alsu@gmail.com");
        alsu.setAge(30);
        alsu.setPassword(springSecurityConfig.passwordEncoder().encode("200"));
        alsu.setRoles(roleUser);

        userService.saveUser(hren);
        userService.saveUser(alsu);

        ObjectMapper objectMapper = new ObjectMapper();
        File file = ResourceUtils
                .getFile("D:\\KATA_3.1.1\\src\\main\\resources\\newjsonlist.json");
        try {
            List<User> users = objectMapper.readValue(file, new TypeReference<>() {
            });
            users.forEach(u -> u.setRoles(roleUser));
            //users.forEach(user1 -> user1.setPassword(springSecurityConfig.passwordEncoder().encode(user1.getPassword())));
            userRepository.saveAll(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
