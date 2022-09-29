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
    public String home(Model model, Principal principal) {
        User connectedUser = userService.getUserByEmail(principal.getName());
        BankAccount accountToFind = connectedUser.getBankAccount();
        // Pas de compte bancaire enregistré
        if (accountToFind == null) {
            model.addAttribute("user", connectedUser);
            logger.info("No bank account recorded");
            return "home";
        }
        // Existence compte bancaire
        logger.info("Loading User Information");
        // Chargement données utilisateur
        model.addAttribute("user", connectedUser);
        // Chargement données bancaires du User connecté
        model.addAttribute("bankAccount", accountToFind);
        // Chargement Valeur Balance
        Float balance = connectedUser.getBalance();
        model.addAttribute("balance", balance);
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
    //public ModelAndView addbank(@ModelAttribute BankAccount bankAccount, Principal principal, RedirectAttributes redirAttrs) {
    public ModelAndView addbank(String bankname, String iban, String bic, Principal principal, RedirectAttributes redirAttrs) {

        // Recherche compte bancaire pre-existant
        User userBank = userService.getUserByEmail(principal.getName());
        BankAccount bankAccountToFind = userBank.getBankAccount();

        // Pas de compte bancaire => CREATION
        if (bankAccountToFind == null) {
            BankAccount BankAccount = new BankAccount(bankname, iban, bic, userBank);
            bankAccountService.addBankAccount(BankAccount);
            logger.info("New bankAccount saved");
            //redirAttrs.addFlashAttribute("bankaccountAdded", successString);
            return new ModelAndView("redirect:/home");
        }
        // compte bancaire existant => UPDATE
        bankAccountToFind.setBankName(bankname);
        bankAccountToFind.setIban(iban);
        bankAccountToFind.setBic(bic);
        bankAccountService.updateBankAccount(userBank.getBankAccount().getBankAccountId(), bankAccountToFind);
        //redirAttrs.addFlashAttribute("bankaccountUpdate", "Update!");
        logger.info("BankAccount updated & saved");
        return new ModelAndView("redirect:/home");
    }
}
