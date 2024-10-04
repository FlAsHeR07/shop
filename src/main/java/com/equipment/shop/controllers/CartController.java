package com.equipment.shop.controllers;

import com.equipment.shop.dao.GoodRepository;
import com.equipment.shop.dao.UserRepository;
import com.equipment.shop.models.Cart;
import com.equipment.shop.models.OrderGenerator;
import com.equipment.shop.models.User;
import com.equipment.shop.services.CartService;

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
    private final CartService cartService;


    @Autowired
    public CartController(GoodRepository goodRepository, UserRepository userRepository, OrderGenerator orderGenerator, CartService cartService) {
        this.goodRepository = goodRepository;
        this.userRepository = userRepository;
        this.orderGenerator = orderGenerator;
        this.cartService = cartService;
    }

    @GetMapping("/add")
    public String addToCart(HttpSession httpSession, @RequestParam("quantity") int quantity, Model model) {
        User userSession = (User) httpSession.getAttribute("currentUser");
        User currentUser = userRepository.findUserById(userSession.getId());
        Long itemId = (Long) httpSession.getAttribute("openedItemId");
        Cart cart = currentUser.getCart();
        Integer currentQuantity = cart.getItems().get(goodRepository.getReferenceById(itemId));
        if (currentQuantity == null) {
            cart.getItems().put(goodRepository.getReferenceById(itemId), quantity);
        } else {
            cart.getItems().put(goodRepository.getReferenceById(itemId), currentQuantity + quantity);
        }
        userRepository.saveAndFlush(currentUser);
        return "redirect:/goods";
    }

    @GetMapping()
    public String showCart(HttpSession httpSession, Model model) {
        User userSession = (User) httpSession.getAttribute("currentUser");
        User currentUser = userRepository.findUserById(userSession.getId());
        Cart cart = currentUser.getCart();
        double price = cart.calculatePrice();

        model.addAttribute("cart", cart);
        model.addAttribute("price", price);
        model.addAttribute("liqpayForm", orderGenerator.createPaymentFormHtml(httpSession, price));

        return "orders/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession httpSession, Model model) {
        User userSession = (User) httpSession.getAttribute("currentUser");
        User currentUser = userRepository.findUserById(userSession.getId());
        currentUser.getCart().getItems().clear();
        return "redirect:/goods";
    }
}
