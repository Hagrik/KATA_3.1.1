package ru.hagrik.firstboot.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hagrik.firstboot.model.User;

import java.util.List;

public interface UserService extends UserDetailsService{

    void saveUser(User user);

    List<User> findAllUsers() ;

    User findUserById(Long id) ;

    void removeUserById(Long id);

    @Override
    UserDetails loadUserByUsername(String name) throws UsernameNotFoundException;

}
