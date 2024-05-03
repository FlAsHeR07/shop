package com.equipment.shop.dao;

import com.equipment.shop.models.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM goods";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                goods.add(new Good(
                        resultSet.getInt("good_id"),
                        resultSet.getString("name"),
                        resultSet.getLong("price_kopeck"),
                        resultSet.getString("description"),
                        resultSet.getInt("category_id")
                ));
            }
            return goods;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Good getGoodById(int id) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM goods WHERE good_id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            Good good = new Good(
                    resultSet.getInt("good_id"),
                    resultSet.getString("name"),
                    resultSet.getLong("price_kopeck"),
                    resultSet.getString("description"),
                    resultSet.getInt("category_id")
            );

            return new Good(
                        resultSet.getInt("good_id"),
                        resultSet.getString("name"),
                        resultSet.getLong("price_kopeck"),
                        resultSet.getString("description"),
                        resultSet.getInt("category_id")
                );
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
