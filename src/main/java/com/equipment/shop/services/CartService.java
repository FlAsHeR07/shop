package com.equipment.shop.services;

import com.equipment.shop.dao.UserRepository;
import com.equipment.shop.models.Cart;
import com.equipment.shop.models.User;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;

@Transactional
@Service
public class CartService {

    private final UserRepository userRepository;

    @Autowired
    public CartService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Cart getCartWithItems(Long userId) {
        User user = userRepository.findUserById(userId);
        Cart cart = user.getCart();
        Hibernate.initialize(cart.getItems());

        // Возможно, здесь нужно добавить явную инициализацию товаров в корзине
        cart.getItems().forEach((good, quantity) -> {
            Hibernate.initialize(good); // Инициализируем ленивую загрузку
        });

        cart.calculatePrice();

        return cart;
    }
}