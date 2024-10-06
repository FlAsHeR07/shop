package com.equipment.shop.services;

import com.equipment.shop.dao.CartRepository;
import com.equipment.shop.dao.GoodRepository;
import com.equipment.shop.models.Cart;
import com.equipment.shop.models.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartService {

    private final GoodRepository goodRepository;
    private final CartRepository cartRepository;

    @Autowired
    public CartService(GoodRepository goodRepository, CartRepository cartRepository) {
        this.goodRepository = goodRepository;
        this.cartRepository = cartRepository;
    }

    public void clearCart(Cart cart) {
        for (Map.Entry<Good, Integer> entry : cart.getItems().entrySet()) {
            Good good = entry.getKey();
            good.setQuantity(good.getQuantity() + entry.getValue());
            goodRepository.saveAndFlush(good);
        }
        cart.getItems().clear();
        cartRepository.saveAndFlush(cart);
    }
}