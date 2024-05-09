package com.equipment.shop.controllers;

import com.equipment.shop.dao.CartDAO;
import com.equipment.shop.dao.GoodDAO;
import com.equipment.shop.models.Good;
import com.equipment.shop.models.OrderGenerator;
import com.equipment.shop.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartDAO cartDAO;
    private final GoodDAO goodDAO;
    private final OrderGenerator orderGenerator;

    @Autowired
    public CartController(CartDAO cartDAO, GoodDAO goodDAO, OrderGenerator orderGenerator) {
        this.cartDAO = cartDAO;
        this.goodDAO = goodDAO;
        this.orderGenerator = orderGenerator;
    }

    @GetMapping("/add")
    public String addToCart(HttpSession httpSession, @RequestParam("quantity") int quantity, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser");
        Integer itemId = (Integer) httpSession.getAttribute("openedItemId");
        Map<Integer, Integer> cart = cartDAO.getCart(currentUser);
        Integer currentQuantity = cart.get(itemId);
        if (currentQuantity == null) {
            cart.put(itemId, quantity);
        } else {
            cart.put(itemId, currentQuantity + quantity);
        }
        cartDAO.setCart(currentUser, cart);
        return "redirect:/goods";
    }

    @GetMapping()
    public String showCart(HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser");
        Map<Integer, Integer> cartIntegers = cartDAO.getCart(currentUser);
        Map<String, Integer> cart = new HashMap<>();
        long kopecks = 0;
        for (Map.Entry<Integer, Integer> entry : cartIntegers.entrySet()) {
            Good currentGood = goodDAO.getGoodById(entry.getKey());
            cart.put(currentGood.getName(), entry.getValue());
            kopecks += ((long) entry.getValue()) * currentGood.getPrice_kopeck();
        }
        double uah = ((double)kopecks) / 100;
        model.addAttribute("cart", cart);
        model.addAttribute("price", uah);
        model.addAttribute("liqpayForm", orderGenerator.createPaymentFormHtml(uah));

        return "orders/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser");
        cartDAO.setCart(currentUser, null);
        return "redirect:/goods";
    }
}
