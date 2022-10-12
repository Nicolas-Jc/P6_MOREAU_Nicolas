package com.openclassrooms.paymybuddy.controller;


import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
public class ContactController {
    private static final Logger logger = LogManager.getLogger("ContactController");
    private static final String REDIRECT_CONTACTS = "redirect:/contacts";

    @Autowired
    private final UserService userService;

    public ContactController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findContact")
    public String findContact(@RequestParam(value = "email") String email,
                              RedirectAttributes redirAttrs) {
        User contactToFind = userService.getUserByEmail(email);
        if (contactToFind == null) {
            redirAttrs.addFlashAttribute("searchError", "Email not found");
            logger.warn("Email not found: {}", email);
            return REDIRECT_CONTACTS;
        }
        redirAttrs.addFlashAttribute("searchContact", contactToFind);
        logger.info("Email address found: {}", email);
        return REDIRECT_CONTACTS;
    }

    @PostMapping("/contacts")
    public String addContactToList(Model model, @RequestParam(value = "email") String email,
                                   Principal user, RedirectAttributes redirAttrs) {
        User userConnected = userService.getUserByEmail(user.getName());
        User contactToAdd = userService.getUserByEmail(email);
        if (contactToAdd == null || userConnected.getContactsList().contains(contactToAdd)) {
            logger.warn("Email Error, user not found OR user already in Contacts List");
            redirAttrs.addFlashAttribute("addError", "Email Error, user not found OR user already in Contacts List");
            return REDIRECT_CONTACTS;
        }
        userService.addContact(userConnected, contactToAdd);
        logger.info("Contact added to list");
        model.addAttribute("addContactSuccess",
                "Contact have been added to your list");
        return REDIRECT_CONTACTS;
    }

    @GetMapping("/contacts")
    public String loadContactsList(Model model, Principal user) {
        String userEmail = user.getName();
        User userConnected2 = userService.getUserByEmail(userEmail);
        List<User> contactsList = userConnected2.getContactsList();
        model.addAttribute("contactsList", contactsList);
        logger.info("Contacts loaded");
        return "contacts";
    }
}
