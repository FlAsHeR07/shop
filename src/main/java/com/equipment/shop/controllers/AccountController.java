package com.equipment.shop.controllers;

import com.equipment.shop.dao.UserRepository;
import com.equipment.shop.dto.UserDTO;
import com.equipment.shop.exceptions.AuthenticationException;
import com.equipment.shop.exceptions.RegistrationException;
import com.equipment.shop.models.User;
import com.equipment.shop.services.AccountService;
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
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login/form")
    public String loginForm(HttpSession httpSession, Model model) {
        model.addAttribute("userDTO", new UserDTO());
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
        model.addAttribute("userDTO", new UserDTO());
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
    public String login(HttpSession httpSession, @ModelAttribute("userDTO") UserDTO userDTO) {
        try {
            accountService.tryLogIn(userDTO);
        } catch (AuthenticationException e) {
            httpSession.setAttribute("authenticationWarning", e);
            return "redirect:/account/login/form";
        }
        httpSession.setAttribute("currentUser", accountService.getUser(userDTO));
        httpSession.setAttribute("authenticationWarning", "");
        return "redirect:/account/current";
    }

    @PostMapping("/signup")
    public String signup(HttpSession httpSession, @ModelAttribute("userDTO") UserDTO userDTO) {
        try {
            accountService.trySignUp(userDTO);
        } catch (RegistrationException e) {
            httpSession.setAttribute("authenticationWarning", e);
            return "redirect:/account/signup/form";
        }
        httpSession.setAttribute("currentUser", accountService.getUser(userDTO));
        httpSession.setAttribute("authenticationWarning", "");
        return "redirect:/account/current";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.setAttribute("currentUser", null);
        return "redirect:/account/current";
    }
}
