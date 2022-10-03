package com.openclassrooms.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    /*@GetMapping(value = "/error")
    public String unauthorizedAccess(Model model) {
        model.addAttribute("message", "You not have permission to access this page !");
        logger.error("Error unauthorized Access");
        return "error";
    }*/

}