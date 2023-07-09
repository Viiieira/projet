package com.example.web.controllers;

import com.example.web.models.ProductCategoryEntity;
import com.example.web.models.UsersModel;
import com.example.web.services.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.web.repository.CategoryProductsRepository;

import java.util.List;

@Controller
@SessionAttributes("userLogin")
public class UsersController {

    private final UsersService usersService;
    private final CategoryProductsRepository categoryRepository;

    public UsersController(UsersService usersService, CategoryProductsRepository categoryRepository) {

        this.usersService = usersService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/personal_page")
    public String getPersonalPage(Model model, SessionStatus sessionStatus) {
        if (!model.containsAttribute("userLogin")) {
            System.out.println("User didn't log in, redirecting to login...");
            return logout(sessionStatus);
        } else {
            return "personal_page";
        }
    }


    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        if (model.containsAttribute("userLogin")) {
            System.out.println("User already logged in, redirecting to personal page...");
            return "redirect:/personal_page";
        }
        model.addAttribute("registerRequest", new UsersModel());
        return "register_page";
    }

    @GetMapping("/products")
    public String getProductspage(Model model) {
        if (!model.containsAttribute("userLogin")) {
            System.out.println("No logged user, redirecting to login");
            return "redirect:/login";
        }

        List<ProductCategoryEntity> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "products_page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        if (model.containsAttribute("userLogin")) {
            System.out.println("User already logged in, redirecting to personal page...");
            return "redirect:/personal_page";
        }
        model.addAttribute("loginRequest", new UsersModel());
        return "login_page";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UsersModel usersModel, RedirectAttributes redirectAttributes) {
        UsersModel registeredUser = usersService.registerUser(usersModel.getName(), usersModel.getEmail(), usersModel.getNif(), usersModel.getPhone(), usersModel.getPassword());
        if (registeredUser == null) {
            redirectAttributes.addFlashAttribute("errorRegister", "Please check your information and try again");
            return "redirect:/register";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UsersModel usersModel, RedirectAttributes redirectAttributes) {
        UsersModel authenticated = usersService.authenticate(usersModel.getName(), usersModel.getPassword());
        if (authenticated == null) {
            redirectAttributes.addFlashAttribute("errorLogin", "Invalid username or password");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("userLogin", authenticated);
            return "redirect:/personal_page";
        }
    }
}
