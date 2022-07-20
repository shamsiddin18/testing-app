package com.testapp.user.controller;

import com.testapp.user.model.UserModel;
import com.testapp.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import javax.validation.Valid;

@Controller
public final class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model, Principal principal) {
        if (principal.getName() != null) {
            return "redirect:/";
        }

        model.addAttribute("userModel", new UserModel());
        return "user/register";
    }

    @GetMapping("/")
    public String homePage(Model model, Principal principal) {
        model.addAttribute("userLogin", principal.getName());
        return "user/home";
    }

    @PostMapping("/register")
    public String submitRegisterForm(@Valid UserModel userModel, BindingResult result) {
        if (result.hasErrors()) {
            return "user/register";
        }
        userService.registerUser(userModel);
        return "redirect:/login";
    }
}
