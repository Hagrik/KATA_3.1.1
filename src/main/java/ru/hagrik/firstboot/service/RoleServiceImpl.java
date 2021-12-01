package ru.hagrik.firstboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hagrik.firstboot.model.Role;
import ru.hagrik.firstboot.repository.RoleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    final RoleRepository roleRepository;
    @PersistenceContext
    final EntityManager entityManager;

    public RoleServiceImpl(RoleRepository roleRepository, EntityManager entityManager) {
        this.roleRepository = roleRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveRole(Role role){
        roleRepository.save(role);
    }

    @Transactional
    public List<Role> findAllRoles() {
        return entityManager.createQuery("FROM Role").getResultList();
    }

    @Transactional
    public Role getRoleByRoleName(String roleName) {
        return entityManager.createQuery(
                        "FROM Role WHERE role=:role", Role.class)
                .setParameter("role", roleName).getSingleResult();
    }
}
