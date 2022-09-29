package com.openclassrooms.paymybuddy.controller;


import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubscribeController {

    private static Logger logger = LogManager.getLogger("SubscribeController");

    @Autowired
    UserService userService;

    // Constructeur

    public SubscribeController(UserService userService) {
        this.userService = userService;
    }

    // Chargement de la page:
    @GetMapping(value = "/subscribe")
    public String subscribeView(Model model) {
        model.addAttribute("user", new User());
        return "subscribe";
    }

    // - create a new user
    @PostMapping(value = "/subscribe")
    public String subscribe(@Validated User user, BindingResult result, RedirectAttributes redirAttrs) {
        String err = userService.validateUser(user);
        if (!"Not Found".equals(err)) {
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", err);
            logger.info("Data error, no subscribe");
            return "redirect:/subscribe";
        }
        redirAttrs.addFlashAttribute("success", "Success!");
        logger.info("New user to save: {}", user);
        userService.addUser(user);
        return "redirect:/login";
    }
}
