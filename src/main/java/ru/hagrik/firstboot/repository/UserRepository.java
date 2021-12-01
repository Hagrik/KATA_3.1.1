package ru.hagrik.firstboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hagrik.firstboot.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
