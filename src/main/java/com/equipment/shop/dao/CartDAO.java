package com.equipment.shop.dao;

import com.equipment.shop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CartDAO {
    private final ApplicationContext applicationContext;
    private final Connection connection;

    @Autowired
    public CartDAO(ApplicationContext applicationContext, Connection connection) {
        this.applicationContext = applicationContext;
        this.connection = connection;
    }

    public Map<Integer, Integer> getCart(int  user_id) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT cart FROM users WHERE user_id = ?;");
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            byte [] bytes = rs.getBytes("cart");
            rs.close();
            ps.close();
            ObjectInputStream in = null;
            Map<Integer, Integer> cart = null;
            if (bytes != null) {
                in = new ObjectInputStream(new ByteArrayInputStream(bytes));
                cart = (HashMap<Integer, Integer>) in.readObject();
            }
            if (in != null) {
                in.close();
                return cart;
            }
            return new HashMap<>();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCart(int user_id, Map<Integer, Integer> cart) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET cart = ? WHERE user_id = ?;");
            ps.setInt(2, user_id);
            if (cart == null) {
                ps.setBytes(1, null);
                ps.execute();
                ps.close();
                return;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(cart);
            out.flush();
            ps.setBytes(1, baos.toByteArray());
            ps.execute();
            out.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
