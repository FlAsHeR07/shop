package com.equipment.shop.dao;

import com.equipment.shop.models.Good;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodRepository extends JpaRepository<Good, Long> {

}
