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

    private static final Logger logger = LogManager.getLogger("SubscribeController");

    @Autowired
    UserService userService;

    public SubscribeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/subscribe")
    public String subscribeView(Model model) {
        model.addAttribute("user", new User());
        return "subscribe";
    }

    @PostMapping(value = "/subscribe")
    public String subscribe(@Validated User user, BindingResult result, RedirectAttributes redirAttrs) {
        Boolean verifNewUser = userService.verifNewUser(user);
        if (Boolean.TRUE.equals(verifNewUser)) {
            ObjectError error = new ObjectError("globalError", "Email is already used!");
            result.addError(error);

        }
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Email is already used!");
            logger.error("Error, no subscribe");
            return "redirect:/subscribe";
        }
        redirAttrs.addFlashAttribute("success", "Success!");
        userService.addUser(user);
        logger.info("New user saved : {}", user);
        return "redirect:/login";
    }
}
