package com.equipment.shop.controllers;

import com.equipment.shop.dao.CartDAO;
import com.equipment.shop.dao.GoodDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrdersController {

    private final CartDAO cartDAO;
    private final GoodDAO goodDAO;

    @Autowired
    public OrdersController(CartDAO cartDAO, GoodDAO goodDAO) {
        this.cartDAO = cartDAO;
        this.goodDAO = goodDAO;
    }

    @PostMapping("/order/payment")
    public void paymentAnalyze(
            @RequestParam("status") String status,
            @RequestParam("amount") double amount
    ) {
        System.out.println("Callback is caught!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
