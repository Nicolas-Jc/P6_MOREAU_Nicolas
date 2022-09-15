package com.openclassrooms.paymybuddy.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    private static Logger logger = LogManager.getLogger("LoginController");

    @GetMapping("/login")
    public String login() {
        logger.info("GetMapping /login");
        return "login";
    }

}