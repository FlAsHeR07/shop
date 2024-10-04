package com.equipment.shop.controllers;

import com.equipment.shop.dao.CartRepository;
import com.equipment.shop.dao.OrderRepository;
import com.equipment.shop.dao.UserRepository;
import com.equipment.shop.models.Cart;
import com.equipment.shop.models.Order;
import com.equipment.shop.models.User;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Controller
public class OrdersController {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    public OrdersController(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/order/new")
    public String showSuccessPage() {
        return "order/payment";
    }

    @GetMapping("/orders")
    public String showAllOrders(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("currentUser");
        List<Order> orders = orderRepository.findAllByUser(user);
        model.addAttribute("orders", orders);
        return "order/all_orders";
    }


    @PostMapping("/order/payment")
    public void paymentAnalyze(
            @RequestParam("data") String data,
            @RequestParam("signature") String signature
    ) {
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader(
                    new ByteArrayInputStream(Base64.getDecoder().decode(data))));
            JSONObject jsonObject = new JSONObject(reader.readLine());
            int user_id = Integer.parseInt(jsonObject.getString("info"));
            User currentUser = userRepository.findUserById(user_id);
            Cart cartForUser = currentUser.getCart();
            Cart cartForOrder = new Cart(cartForUser);
            Order order = new Order(cartForOrder.getItems(), currentUser, new Date());

            cartRepository.saveAndFlush(cartForOrder);
            orderRepository.saveAndFlush(order);
            currentUser.getOrders().add(order);
            cartForUser.getItems().clear();
            cartRepository.saveAndFlush(cartForUser);
            userRepository.saveAndFlush(currentUser);
            logger.info("Callback is caught from liqpay");
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}


/*
* {"payment_id":2460231630,
* "action":"pay",
* "status":"sandbox",
* "version":3,
* "type":"buy",
* "paytype":"card",
* "public_key":"sandbox_i48897120598",
* "acq_id":414963,
* "order_id":"-99979",
* "liqpay_order_id":"372OO6XK1715333166936842",
* "description":"Оплата обраних товарів",
* "sender_first_name":"Name",
* "sender_last_name":"Surname",
* "sender_card_mask2":"424242*42",
* "sender_card_bank":"Test",
* "sender_card_type":"visa",
* "sender_card_country":804,
* "ip":"194.53.197.20",
* "amount":1360.74,
* "currency":"UAH",
* "sender_commission":0.0,
* "receiver_commission":20.41,
* "agent_commission":0.0,
* "amount_debit":1360.74,
* "amount_credit":1360.74,
* "commission_debit":0.0,
* "commission_credit":20.41,
* "currency_debit":"UAH",
* "currency_credit":"UAH",
* "sender_bonus":0.0,
* "amount_bonus":0.0,
* "mpi_eci":"7",
* "is_3ds":false,
* "language":"uk",
* "create_date":1715333166940,
* "end_date":1715333167092,
* "transaction_id":2460231630
* }*/