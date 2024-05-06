package com.equipment.shop.dao;

import com.equipment.shop.models.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoodDAO {
    private final ApplicationContext applicationContext;
    private final Connection connection;

    @Autowired
    public GoodDAO(ApplicationContext applicationContext, Connection connection) {
        this.applicationContext = applicationContext;
        this.connection = connection;
    }

    public List<Good> getAllGoods() {
        List<Good> goods = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT good_id, name, price_kopeck, description, category_id, encode(image, 'base64') AS imageCode FROM goods ORDER BY good_id ASC;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                goods.add(new Good(
                        rs.getInt("good_id"),
                        rs.getString("name"),
                        rs.getLong("price_kopeck"),
                        rs.getString("description"),
                        rs.getInt("category_id"),
                        rs.getString("imageCode")
                ));
            }
            rs.close();
            ps.close();
            return goods;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Good getGoodById(int id) {
        Good good;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT good_id, name, price_kopeck, description, category_id, encode(image, 'base64') AS imageCode FROM goods where good_id = " + id + ";");
            ResultSet rs = ps.executeQuery();
            rs.next();
            good = new Good(
                    rs.getInt("good_id"),
                    rs.getString("name"),
                    rs.getLong("price_kopeck"),
                    rs.getString("description"),
                    rs.getInt("category_id"),
                    rs.getString("imageCode")
            );
            rs.close();
            ps.close();
            return good;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
