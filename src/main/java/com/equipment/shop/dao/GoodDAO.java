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
                PreparedStatement prepStIds = connection.prepareStatement("SELECT * FROM goods_manufacturers WHERE good_id = ?;");
                prepStIds.setInt(1, rs.getInt("good_id"));
                ResultSet resultIds = prepStIds.executeQuery();
                List<String> manufacturers = new ArrayList<>();
                while (resultIds.next()) {
                    PreparedStatement prepStNames = connection.prepareStatement("SELECT * FROM manufacturers WHERE manufacturer_id = ?;");
                    prepStNames.setInt(1, resultIds.getInt("manufacturer_id"));
                    ResultSet resultNames = prepStNames.executeQuery();
                    if (resultNames.next()) {
                        manufacturers.add(resultNames.getString("name"));
                    }
                }
                goods.add(new Good(
                        rs.getInt("good_id"),
                        rs.getString("name"),
                        rs.getLong("price_kopeck"),
                        rs.getString("description"),
                        rs.getInt("category_id"),
                        rs.getString("imageCode"),
                        manufacturers
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
            PreparedStatement ps = connection.prepareStatement("SELECT good_id, name, price_kopeck, description, category_id, encode(image, 'base64') AS imageCode FROM goods where good_id = ?;");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            PreparedStatement prepStIds = connection.prepareStatement("SELECT * FROM goods_manufacturers WHERE good_id = ?;");
            prepStIds.setInt(1, rs.getInt("good_id"));
            ResultSet resultIds = prepStIds.executeQuery();
            List<String> manufacturers = new ArrayList<>();
            while (resultIds.next()) {
                PreparedStatement prepStNames = connection.prepareStatement("SELECT * FROM manufacturers WHERE manufacturer_id = ?;");
                prepStNames.setInt(1, resultIds.getInt("manufacturer_id"));
                ResultSet resultNames = prepStNames.executeQuery();
                if (resultNames.next()) {
                    manufacturers.add(resultNames.getString("name"));
                }
            }
            good = new Good(
                    rs.getInt("good_id"),
                    rs.getString("name"),
                    rs.getLong("price_kopeck"),
                    rs.getString("description"),
                    rs.getInt("category_id"),
                    rs.getString("imageCode"),
                    manufacturers
            );
            rs.close();
            ps.close();
            return good;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAvailableInStock(Good good) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT quantity FROM goods where good_id = ?;");
            ps.setInt(1, good.getGood_id());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("quantity") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCategory(Good good) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT category_id FROM goods where good_id = ?;");
            ps.setInt(1, good.getGood_id());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int category_id = rs.getInt("category_id");
            PreparedStatement ps2 = connection.prepareStatement("SELECT name FROM categories where category_id = ?;");
            ps2.setInt(1, category_id);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            return rs2.getString("name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
