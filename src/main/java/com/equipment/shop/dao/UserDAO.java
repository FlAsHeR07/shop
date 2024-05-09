package com.equipment.shop.dao;

import com.equipment.shop.exceptions.AuthenticationException;
import com.equipment.shop.exceptions.RegistrationException;
import com.equipment.shop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDAO {
    private final ApplicationContext applicationContext;
    private final Connection connection;

    @Autowired
    public UserDAO(ApplicationContext applicationContext, Connection connection) {
        this.applicationContext = applicationContext;
        this.connection = connection;
    }

    private void createUser(User user) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (username, password, email, full_name) VALUES (?, ?, ?, ?);");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void trySignUp(User user) throws RegistrationException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users;");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                if (rs.getString("username").equals(user.getUsername()))
                    throw new RegistrationException("Username вже використовується!");
                if (rs.getString("email").equals(user.getEmail()))
                    throw new RegistrationException("Email вже використовується!");
            }
            if(!user.getUsername().matches("^[a-zA-Z][a-zA-Z0-9_]*$") || user.getUsername().length() < 8)
                throw new RegistrationException("Username має починатись з літери a–z A–Z, може містити лише букви a–z A–Z 0–9 без пробілів, всього не менше 8 символів!");
            if(user.getPassword().matches("[^a-zA-Z\\d]") || user.getPassword().length() < 8)
                throw new RegistrationException("Пароль може містити лише букви a–z A–Z 0–9 без пробілів та не менше 8 символів!");
            if(!user.getEmail().matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$"))
                throw new RegistrationException("Email не дійсний!");
            if(!user.getFullName().matches("^[а-яА-ЯєЄіІїЇёЁ]+(\\s[а-яА-ЯєЄіІїЇёЁ]+){1,2}$|^[a-zA-Z]+(\\s[a-zA-Z]+){1,2}$"))
                throw new RegistrationException("ПІБ не валідне!");
            createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void tryLogIn(User user) throws AuthenticationException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            boolean userFound = false;
            if (rs.next()) {
                if (rs.getString("username").equals(user.getUsername()) &&
                    rs.getString("password").equals(user.getPassword()))
                    userFound = true;
            }
            if (!userFound)
                throw new AuthenticationException("Username відсутній або password не коректний!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(String username, String password) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?;")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}