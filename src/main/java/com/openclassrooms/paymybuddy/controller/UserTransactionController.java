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
    private static final String REDIRECT_TRANSAC = "redirect:/transactions";
    private static final String ERROR_AMOUNT = "errorAmount";

    @Autowired
    private UserTransactionService userTransactionService;
    @Autowired
    private UserService userService;

    public UserTransactionController(UserService userService, UserTransactionService userTransactionService) {
        this.userService = userService;
        this.userTransactionService = userTransactionService;
    }

    @GetMapping("/transactions")
    public String transactionsLoad(Model model, Principal principal) {
        String userEmail = principal.getName();
        User userConnected = userService.getUserByEmail(userEmail);
        List<User> contactsList = userConnected.getContactsList();

        model.addAttribute("user", userConnected);
        model.addAttribute("contact", contactsList);
        return "transactions";
    }

    @GetMapping("/mypayments")
    public String myPaymentsLoad(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model, Principal principal) {

        String userEmail = principal.getName();
        User userConnected = userService.getUserByEmail(userEmail);
        Paged<UserTransaction> transactionsSent = userTransactionService.getTransacsBySenderPaginated(userConnected, pageNumber, size);

        model.addAttribute("transactionsSent", transactionsSent);
        logger.info("Transactions uploaded for user: {}", userConnected);
        return "mypayments";
    }

    @GetMapping("/myrefunds")
    public String myRefundsLoad(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model, Principal principal) {

        String userEmail = principal.getName();
        User userConnected = userService.getUserByEmail(userEmail);
        Paged<UserTransaction> transactionsReceived = userTransactionService.getTransacsByReceiverPaginated(userConnected, pageNumber, size);
        model.addAttribute("transactionsReceiver", transactionsReceived);
        logger.info("Transactions uploaded for user: {}", userConnected);
        return "myrefunds";
    }

    @PostMapping("/sendmoney")
    public String sendmoney(String emailReceiver, String description, String amount,
                            Principal principal, RedirectAttributes redirAttrs) {
        String userEmail = principal.getName();
        User userConnected = userService.getUserByEmail(userEmail);

        try {
            float sendMoneyToCheck = Float.parseFloat(amount);

            if (sendMoneyToCheck == 0) {
                return REDIRECT_TRANSAC;
            }

            if (sendMoneyToCheck < 0) {
                redirAttrs.addFlashAttribute(ERROR_AMOUNT, "Negative amount not possible");
                return REDIRECT_TRANSAC;
            }

            Float balance = userConnected.getBalance();
            if (sendMoneyToCheck > balance) {
                redirAttrs.addFlashAttribute(ERROR_AMOUNT, "The amount to transfer exceeds your balance");
                return REDIRECT_TRANSAC;
            }

            userTransactionService.sendMoney(userConnected, emailReceiver, description, sendMoneyToCheck);
            redirAttrs.addFlashAttribute("transactionSuccess", "OK !");
            logger.info("User transaction sent to: {}", emailReceiver);
            return REDIRECT_TRANSAC;

        } catch (NumberFormatException e) {
            redirAttrs.addFlashAttribute(ERROR_AMOUNT, "Only numerical values are allowed");
            return REDIRECT_TRANSAC;
        }
    }
}