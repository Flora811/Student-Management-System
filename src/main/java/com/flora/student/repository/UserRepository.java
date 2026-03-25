package com.flora.student.repository;

import com.flora.student.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    public boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);
}
