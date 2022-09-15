package com.openclassrooms.paymybuddy.controller;


import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Controller
public class HomeController {

    private static Logger logger = LogManager.getLogger("HomeController");

    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    UserService userService;

    public HomeController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    /*@ModelAttribute("bankAccount")
    public BankAccount bankAccount() {
        return new BankAccount();
    }*/

    @GetMapping({"/", "/home"})
    public String home(Model model, User user) {
        User connectedUser = userService.getUserByEmail(user.getEmail());
        BankAccount accountToFind = connectedUser.getBankAccount();
        if (accountToFind == null) {
            model.addAttribute("user", connectedUser);
            logger.info("No bank account recorded");
            return "home";
        }
        logger.info("Loading User Information");
        // Chargement données utilisateur
        model.addAttribute("user", connectedUser);
        // Chargement données bancaires
        model.addAttribute("bankAccount", accountToFind);
        return "home";
    }

    /*@GetMapping("/home")
    public String home() {
        logger.info("GetMapping home");
        return "home";
    }*/

    /*@PostMapping("/addBankAccount")
    public ModelAndView createNewBankAccount(@ModelAttribute BankAccount bankAccount) {
        bankAccountService.addBankAccount(bankAccount);
        return new ModelAndView("redirect:/home");
    }*/

    @PostMapping("/addBankAccount")
    public ModelAndView addbank(@ModelAttribute BankAccount bankAccount, User user) {

        // Recherche compte bancaire pre-existant
        User userBank = userService.getUserByEmail(user.getEmail());
        BankAccount bankAccountToFind = userBank.getBankAccount();

        // Pas de compte bancaire => Création
        if (bankAccountToFind == null) {
            BankAccount BankAccount = new BankAccount(bankAccount.getBankName(), bankAccount.getIban(), bankAccount.getBic(), userBank);
            bankAccountService.addBankAccount(BankAccount);
            logger.info("New bankAccount saved");
            //redirAttrs.addFlashAttribute("bankaccountAdded", successString);
            return new ModelAndView("redirect:/home");
        }
        // compte bancaire existant => Mise à jour
        bankAccountToFind.setBankName(bankAccount.getBankName());
        bankAccountToFind.setIban(bankAccount.getIban());
        bankAccountToFind.setBic(bankAccount.getBic());
        bankAccountService.updateBankAccount(userBank.getBankAccount().getBankAccountId(), bankAccountToFind);
        //redirAttrs.addFlashAttribute("bankaccountUpdate", "Update!");
        logger.info("BankAccount updated & saved");
        return new ModelAndView("redirect:/home");
    }
}
