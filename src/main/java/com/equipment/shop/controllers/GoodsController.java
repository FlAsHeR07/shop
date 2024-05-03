package com.equipment.shop.controllers;

import com.equipment.shop.dao.GoodDAO;
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
    public String showAll(Model model) {
        model.addAttribute("goods", goodDAO.getAllGoods());
        return "goods/goods";
    }

    @GetMapping("/{id}")
    public String showByID(@PathVariable("id") int id, Model model) {
        model.addAttribute(goodDAO.getGoodById(id));
        return "goods/good";
    }

}
