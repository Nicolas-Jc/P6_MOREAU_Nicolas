package com.openclassrooms.paymybuddy.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class LoginController {

    private static Logger logger = LogManager.getLogger("LoginController");

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    //return  Error page url
    @GetMapping(value = "/error")
    public String accessDenied(Model model, Principal principal) {
        String message = "Sorry " + principal.getName()
                + "You not have permission access this page !";
        model.addAttribute("message", message);
        logger.info("Error with user access");
        return "error";
    }

}