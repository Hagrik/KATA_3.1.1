package ru.hagrik.firstboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hagrik.firstboot.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
