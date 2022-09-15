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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubscribeController {

    @Autowired
    UserService userService;

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    private static Logger logger = LogManager.getLogger("SubscribeController");

    /*@GetMapping("/subscribe")
    public String createUserForm() {
        logger.info("GetMapping subscribe");
        return "subscribe";
    }

    @PostMapping("/addUser")
    public ModelAndView createNewUser(@ModelAttribute User user) {

        userService.addUser(user);
        return new ModelAndView("redirect:/subscribe");
    }*/


    // Fourniture des données à la vue
    @GetMapping(value = "/subscribe")
    public String subscribe(Model model) {
        model.addAttribute("user", new User());
        return "subscribe";
    }


    @PostMapping(value = "/subscribe")
    public String subscribeForm(@Validated User user, Model model) {

        // A prévoir - rajout test pre-existence e-mail
        logger.info("New user to save: {}", user);
        userService.addUser(user);
        return "redirect:/login";
    }
}
