package com.equipment.shop.controllers;

import com.equipment.shop.dao.CartDAO;
import com.equipment.shop.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartDAO cartDAO;

    @Autowired
    public CartController(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @GetMapping("/add")
    public String addToCart(HttpSession httpSession, @RequestParam("quantity") int quantity, Model model) {
        User user = (User) httpSession.getAttribute("currentUser");
        Integer itemId = (Integer) httpSession.getAttribute("openedItemId");
        Map<Integer, Integer> cart = cartDAO.getCart(user);
        Integer currentQuantity = cart.get(itemId);
        if (currentQuantity == null) {
            cart.put(itemId, quantity);
        } else {
            cart.put(itemId, currentQuantity + quantity);
        }
        cartDAO.setCart(user, cart);
        return "redirect:/goods";
    }
}
