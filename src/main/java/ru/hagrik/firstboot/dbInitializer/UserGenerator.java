package ru.hagrik.firstboot.dbInitializer;

import org.springframework.stereotype.Component;
import ru.hagrik.firstboot.config.SpringSecurityConfig;
import ru.hagrik.firstboot.model.Role;
import ru.hagrik.firstboot.model.User;
import ru.hagrik.firstboot.service.RoleService;
import ru.hagrik.firstboot.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserGenerator {

    final UserService userService;
    final RoleService roleService;
    final SpringSecurityConfig springSecurityConfig;

    public UserGenerator(UserService userService, RoleService roleService, SpringSecurityConfig springSecurityConfig) {
        this.userService = userService;
        this.roleService = roleService;
        this.springSecurityConfig = springSecurityConfig;
    }

    @PostConstruct
    public void userGenerator() {
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
        hren.setAge(33);
        hren.setPassword(springSecurityConfig.passwordEncoder().encode("100"));
        hren.setRoles(roleAdmin);

        User alsu = new User();
        alsu.setActive(true);
        alsu.setName("Alsu");
        alsu.setLastname("Shaiheeva");
        alsu.setAge(30);
        alsu.setPassword(springSecurityConfig.passwordEncoder().encode("200"));
        alsu.setRoles(roleUser);

        userService.saveUser(hren);
        userService.saveUser(alsu);
    }
}
