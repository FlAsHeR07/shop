package com.equipment.shop.controllers;

import com.equipment.shop.dao.GoodRepository;
import com.equipment.shop.dao.UserRepository;
import com.equipment.shop.models.Cart;
import com.equipment.shop.models.OrderGenerator;
import com.equipment.shop.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final GoodRepository goodRepository;
    private final UserRepository userRepository;
    private final OrderGenerator orderGenerator;

    @Autowired
    public CartController(GoodRepository goodRepository, UserRepository userRepository, OrderGenerator orderGenerator) {
        this.goodRepository = goodRepository;
        this.userRepository = userRepository;
        this.orderGenerator = orderGenerator;
    }

    @GetMapping("/add")
    public String addToCart(HttpSession httpSession, @RequestParam("quantity") int quantity, Model model) {
        //todo cartDAO remove for cartRepository
        User currentUser = (User) httpSession.getAttribute("currentUser");
        Long itemId = (Long) httpSession.getAttribute("openedItemId");
        Cart cart = currentUser.getCart();
        Integer currentQuantity = cart.getCart().get(goodRepository.getReferenceById(itemId));
        if (currentQuantity == null) {
            cart.getCart().put(goodRepository.getReferenceById(itemId), quantity);
        } else {
            cart.getCart().put(goodRepository.getReferenceById(itemId), currentQuantity + quantity);
        }
        userRepository.flush();
        return "redirect:/goods";
    }

    @GetMapping()
    public String showCart(HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser");
        Cart cart = currentUser.getCart();
        double price = cart.calculatePrice();

        model.addAttribute("cart", cart);
        model.addAttribute("price", price);
        model.addAttribute("liqpayForm", orderGenerator.createPaymentFormHtml(httpSession, price));

        return "orders/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser");
        currentUser.getCart().getCart().clear();
        return "redirect:/goods";
    }
}
