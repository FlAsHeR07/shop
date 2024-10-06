package com.equipment.shop.dao;

import com.equipment.shop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByUsernameAndPassword(String username, String password);
    User findUserById(long id);
}
