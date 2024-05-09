package com.equipment.shop.controllers;

import com.equipment.shop.dao.UserDAO;
import com.equipment.shop.exceptions.AuthenticationException;
import com.equipment.shop.exceptions.RegistrationException;
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
        if (httpSession.getAttribute("authenticationWarning") instanceof AuthenticationException) {
            String warn = ((AuthenticationException) httpSession.getAttribute("authenticationWarning")).getMessage();
            model.addAttribute("authenticationWarning", warn);
        }
        else {
            httpSession.setAttribute("authenticationWarning", null);
        }
        return "authentication/log_in";
    }

    @GetMapping("/signup/form")
    public String signupForm(HttpSession httpSession, Model model) {
        model.addAttribute("user", new User());
        if (httpSession.getAttribute("authenticationWarning") instanceof RegistrationException) {
            String warn = ((RegistrationException) httpSession.getAttribute("authenticationWarning")).getMessage();
            model.addAttribute("authenticationWarning", warn);
        } else {
            httpSession.setAttribute("authenticationWarning", null);
        }
        return "authentication/sign_up";
    }

    @GetMapping("/current")
    public String showCurrentUser(HttpSession httpSession, Model model) {
        httpSession.setAttribute("authenticationWarning", null);
        Object o = httpSession.getAttribute("currentUser");
        User user = (User) o;
        if (user == null) {
            return "redirect:/account/login/form";
        }
        model.addAttribute("user", user);
        return "redirect:/goods";
    }

    @GetMapping("/login")
    public String login(HttpSession httpSession, @ModelAttribute("user") User user) {
        try {
            userDAO.tryLogIn(user);
        } catch (AuthenticationException e) {
            httpSession.setAttribute("authenticationWarning", e);
            return "redirect:/account/login/form";
        }
        httpSession.setAttribute("currentUser", userDAO.getUser(user.getUsername(), user.getPassword()));
        httpSession.setAttribute("authenticationWarning", "");
        return "redirect:/account/current";
    }

    @PostMapping("/signup")
    public String signup(HttpSession httpSession, @ModelAttribute("user") User user) {
        try {
            userDAO.trySignUp(user);
        } catch (RegistrationException e) {
            httpSession.setAttribute("authenticationWarning", e);
            return "redirect:/account/signup/form";
        }
        httpSession.setAttribute("currentUser", userDAO.getUser(user.getUsername(), user.getPassword()));
        httpSession.setAttribute("authenticationWarning", "");
        return "redirect:/account/current";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.setAttribute("currentUser", null);
        return "redirect:/account/current";
    }
}
