package com.equipment.shop.controllers;

import com.equipment.shop.dao.UserDAO;
import com.equipment.shop.exception.AuthenticationException;
import com.equipment.shop.exception.RegistrationException;
import com.equipment.shop.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserDAO userDAO;

    @Autowired
    public AccountController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/login/form")
    public String loginForm(HttpSession httpSession, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("authenticationWarning", httpSession.getAttribute("authenticationWarning"));
        return "authentication/log_in";
    }

    @GetMapping("/signup/form")
    public String signupForm(HttpSession httpSession, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("authenticationWarning", httpSession.getAttribute("authenticationWarning"));
        return "authentication/sign_up";
    }

    @GetMapping("/current")
    public String showCurrentUser(HttpSession httpSession, Model model) {
        httpSession.setAttribute("authenticationWarning", "");
        Object o = httpSession.getAttribute("currentUser");
        User user = (User) o;
        if (user == null) {
            return "redirect:/account/login/form";
        }
        model.addAttribute("user", user);
        return "authentication/user";
    }

    @GetMapping("/login")
    public String login(HttpSession httpSession, @ModelAttribute("user") User user, Model model) {
        try {
            userDAO.tryLogIn(user);
        } catch (AuthenticationException e) {
            httpSession.setAttribute("authenticationWarning", e.getMessage());
            return "redirect:/account/login/form";
        }
        httpSession.setAttribute("currentUser", userDAO.getUser(user.getUsername(), user.getPassword()));
        httpSession.setAttribute("authenticationWarning", "");
        return "redirect:/account/current";
    }

    @PostMapping("/signup")
    public String signup(HttpSession httpSession, @ModelAttribute("user") User user, Model model) {
        try {
            userDAO.trySignUp(user);
        } catch (RegistrationException e) {
            httpSession.setAttribute("authenticationWarning", e.getMessage());
            return "redirect:/account/signup/form";
        }
        httpSession.setAttribute("currentUser", userDAO.getUser(user.getUsername(), user.getPassword()));
        httpSession.setAttribute("authenticationWarning", "");
        return "redirect:/account/current";
    }

}
