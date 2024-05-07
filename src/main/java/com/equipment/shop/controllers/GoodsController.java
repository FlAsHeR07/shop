package com.equipment.shop.controllers;

import com.equipment.shop.dao.GoodDAO;
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

    private final GoodDAO goodDAO;

    @Autowired
    public GoodsController(GoodDAO goodDAO) {
        this.goodDAO = goodDAO;
    }

    @GetMapping()
    public String showAll(HttpSession httpSession, Model model) {
        if (httpSession.getAttribute("currentUser") == null) {
            return "redirect:/account/current";
        }
        model.addAttribute("goods", goodDAO.getAllGoods());
        model.addAttribute("user", httpSession.getAttribute("currentUser"));
        return "goods/goods";
    }

    @GetMapping("/{id}")
    public String showByID(HttpSession httpSession, @PathVariable("id") int id, Model model) {
        if (httpSession.getAttribute("currentUser") == null) {
            return "redirect:/account/current";
        }
        Good good = goodDAO.getGoodById(id);
        model.addAttribute("good", good);
        model.addAttribute("isAvailable", goodDAO.isAvailableInStock(good) ? "наявний" : "відсутній");
        model.addAttribute("category", goodDAO.getCategory(good));
        model.addAttribute("user", httpSession.getAttribute("currentUser"));
        httpSession.setAttribute("openedItemId", id);
        return "goods/good";
    }

}
