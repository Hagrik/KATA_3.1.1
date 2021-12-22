package ru.hagrik.firstboot.service;

import ru.hagrik.firstboot.model.Role;

import java.util.List;


public interface RoleService {

    void saveRole(Role role);

    List<Role> findAllRoles();

}
