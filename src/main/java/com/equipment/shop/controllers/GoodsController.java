package com.equipment.shop.controllers;

import com.equipment.shop.dao.GoodRepository;
import com.equipment.shop.models.Good;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final GoodRepository goodRepository;

    @Autowired
    public GoodsController(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    @GetMapping()
    public String showAll(HttpSession httpSession, Model model) {
        if (httpSession.getAttribute("currentUser") == null) {
            return "redirect:/account/current";
        }
        model.addAttribute("goods", goodRepository.findAll());
        model.addAttribute("user", httpSession.getAttribute("currentUser"));
        return "goods/goods";
    }

    @GetMapping("/{id}")
    public String showByID(HttpSession httpSession, @PathVariable("id") long id, Model model) {
        if (httpSession.getAttribute("currentUser") == null) {
            return "redirect:/account/current";
        }
        Good good = goodRepository.getReferenceById(id);
        model.addAttribute("good", good);
        model.addAttribute("isAvailable", good.getQuantity() > 0 ? "наявний" : "відсутній");
        model.addAttribute("category", good.getCategory());
        model.addAttribute("user", httpSession.getAttribute("currentUser"));
        httpSession.setAttribute("openedItemId", id);
        return "goods/good";
    }
}
