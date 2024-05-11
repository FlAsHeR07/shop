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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDAO {
    private final ApplicationContext applicationContext;
    private final Connection connection;
    private final GoodDAO goodDAO;

    @Autowired
    public OrderDAO(ApplicationContext applicationContext, Connection connection, GoodDAO goodDAO) {
        this.applicationContext = applicationContext;
        this.connection = connection;
        this.goodDAO = goodDAO;
    }

    public List<Order> getOrders(User user) {
        try {
            List<Order> orders = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM orders WHERE user_id = ?;");
            ps.setInt(1, user.getUser_id());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getBytes("information") == null) {
                    rs.close();
                    ps.close();
                    return null;
                }
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes("information")));
                Order currentOrder = (Order) ois.readObject();
                Map<String, Integer> cartForView = new HashMap<>();
                for(Map.Entry<Integer, Integer> entry : currentOrder.getCart().entrySet()) {
                    cartForView.put(goodDAO.getGoodById(entry.getKey()).getName(), entry.getValue());
                }
                currentOrder.setCartForView(cartForView);
                orders.add(currentOrder);
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
            if (order.getCart() != null) {
                for (Map.Entry<Integer, Integer> entry : order.getCart().entrySet()) {
                    PreparedStatement getQuantity = connection.prepareStatement("SELECT quantity FROM goods WHERE good_id = ?");
                    getQuantity.setInt(1, entry.getKey());
                    ResultSet resQuantity = getQuantity.executeQuery();
                    resQuantity.next();
                    int quantity = resQuantity.getInt("quantity") - entry.getValue();
                    PreparedStatement updateGoods = connection.prepareStatement("UPDATE goods SET quantity = ? WHERE good_id = ?");
                    updateGoods.setInt(1, quantity);
                    updateGoods.setInt(2, entry.getKey());
                    updateGoods.execute();
                }
            }

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
