package ru.hagrik.firstboot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hagrik.firstboot.model.Role;
import ru.hagrik.firstboot.repository.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void saveRole(Role role){
        roleRepository.save(role);
    }

    @Transactional
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findRoleByRoleName(roleName);
    }
}
