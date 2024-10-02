package com.equipment.shop.models;

import com.equipment.shop.dao.OrderRepository;
import com.liqpay.LiqPay;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class OrderGenerator {
    private final String PUBLIC_KEY;
    private final String PRIVATE_KEY;
    private final OrderRepository orderRepository;

    {
        try {
            Properties properties = new Properties();
            InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
            properties.load(resourceStream);
            PUBLIC_KEY = properties.getProperty("PUBLIC_KEY");
            PRIVATE_KEY = properties.getProperty("PRIVATE_KEY");
            resourceStream.close();
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public OrderGenerator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String createPaymentFormHtml(HttpSession httpSession, double uah) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", String.valueOf(uah));
        params.put("currency", "UAH");
        params.put("description", "Оплата обраних товарів");
        params.put("order_id", String.valueOf(orderRepository.count() + 1));
        params.put("version", "3");
        params.put("sandbox", "1");
        params.put("result_url", "http://electropoint.hopto.org/order/new");
        params.put("server_url", "http://electropoint.hopto.org/order/payment");
        params.put("info", String.valueOf(((User) httpSession.getAttribute("currentUser")).getId()));
        LiqPay liqpay = new LiqPay(PUBLIC_KEY, PRIVATE_KEY);
        return liqpay.cnb_form(params);
    }
}
