package com.equipment.shop.dao;

import com.equipment.shop.models.Order;
import com.equipment.shop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDAO {
    private final ApplicationContext applicationContext;
    private final Connection connection;

    @Autowired
    public OrderDAO(ApplicationContext applicationContext, Connection connection) {
        this.applicationContext = applicationContext;
        this.connection = connection;
    }

    public List<Order> getOrders(User user) {
        try {
            List<Order> orders = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM orders WHERE user_id = ?;");
            ps.setInt(1, user.getUser_id());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes("information")));
                orders.add((Order) ois.readObject());
                ois.close();
            }
            rs.close();
            ps.close();
            return orders;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addOrder(int user_id, Order order) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO orders(user_id, information) VALUES (?, ?);");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous = new ObjectOutputStream(baos);
            ous.writeObject(order);
            ous.flush();
            byte [] information = baos.toByteArray();
            ous.close();
            ps.setInt(1, user_id);
            ps.setBytes(2, information);
            ps.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int lastOrder() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT order_id FROM orders ORDER BY order_id DESC;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            int res = rs.getInt("order_id");
            rs.close();
            ps.close();
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
