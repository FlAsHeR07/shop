package com.equipment.shop.dao;

import com.equipment.shop.models.Order;
import com.equipment.shop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
}
