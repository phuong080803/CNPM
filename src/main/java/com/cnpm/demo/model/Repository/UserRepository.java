package com.cnpm.demo.model.Repository;

import com.cnpm.demo.model.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}

