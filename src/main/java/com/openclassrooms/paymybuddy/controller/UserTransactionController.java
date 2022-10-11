package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserTransaction;
import com.openclassrooms.paymybuddy.model.paging.Paged;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.service.UserTransactionService;

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
public class UserTransactionController {
    private static final Logger logger = LogManager.getLogger("UserTransactionController");

    @Autowired
    private UserTransactionService userTransactionService;
    @Autowired
    private UserService userService;

    // Constructeur
    public UserTransactionController(UserService userService, UserTransactionService userTransactionService) {
        this.userService = userService;
        this.userTransactionService = userTransactionService;
    }

    // Fourniture données à la vue Transactions
    @GetMapping("/transactions")
    public String transactionsLoad(Model model, Principal principal) {
        String userEmail = principal.getName();
        User userConnected = userService.getUserByEmail(userEmail);
        List<User> contactsList = userConnected.getContactsList();

        model.addAttribute("user", userConnected);
        model.addAttribute("contact", contactsList);
        return "transactions";
    }

    // Fourniture données à la vue My Payments
    @GetMapping("/mypayments")
    public String myPaymentsLoad(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "size", required = false, defaultValue = "15") int size, Model model, Principal principal) {

        String userEmail = principal.getName();
        User userConnected = userService.getUserByEmail(userEmail);
        Paged<UserTransaction> transactionsSent = userTransactionService.getTransacsBySenderPaginated(userConnected, pageNumber, size);

        model.addAttribute("transactionsSent", transactionsSent);
        logger.info("Transactions uploaded for user: {}", userConnected);
        return "mypayments";
    }

    // Fourniture données à la vue My Refunds
    @GetMapping("/myrefunds")
    public String myRefundsLoad(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(value = "size", required = false, defaultValue = "15") int size, Model model, Principal principal) {

        String userEmail = principal.getName();
        User userConnected = userService.getUserByEmail(userEmail);
        Paged<UserTransaction> transactionsReceived = userTransactionService.getTransacsByReceiverPaginated(userConnected, pageNumber, size);
        model.addAttribute("transactionsReceiver", transactionsReceived);
        logger.info("Transactions uploaded for user: {}", userConnected);
        return "myrefunds";
    }

    // Formulaire d'envoi d'argent
    @PostMapping("/sendmoney")
    public String sendmoney(String emailReceiver, String description, Float amount,
                            Principal principal, RedirectAttributes redirAttrs) {
        String userEmail = principal.getName();
        User userConnected = userService.getUserByEmail(userEmail);

        if (amount < 0) {
            redirAttrs.addFlashAttribute("errorAmount", "Negative amount not possible");
            return "redirect:/transactions";
        }

        userTransactionService.sendMoney(userConnected, emailReceiver, description, amount);
        redirAttrs.addFlashAttribute("transactionSuccess", "OK !");
        logger.info("User transaction sent to: {}", emailReceiver);
        return "redirect:/transactions";
    }
}