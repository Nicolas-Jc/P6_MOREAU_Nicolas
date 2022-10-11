package com.openclassrooms.paymybuddy.controller;


import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.service.BankTransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Controller
public class HomeController {

    private static final Logger logger = LogManager.getLogger("HomeController");

    static final float MAX_AMOUNT_DEPOSIT = 10000f;

    private String redirectHome = "redirect:/home";

    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    BankTransactionService bankTransactionService;

    @Autowired
    UserService userService;

    public HomeController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model, Principal principal) {
        User connectedUser = userService.getUserByEmail(principal.getName());
        BankAccount accountToFind = connectedUser.getBankAccount();
        // Pas de compte bancaire enregistré
        if (accountToFind == null) {
            model.addAttribute("user", connectedUser);
            logger.warn("No bank account recorded");
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

    @PostMapping("/addBankAccount")
    public ModelAndView addbank(String bankname, String iban, String bic, Principal principal, RedirectAttributes redirAttrs) {

        // Tests completude des 3 champs
        if (bankname.isEmpty() || iban.isEmpty() || bic.isEmpty()) {
            logger.warn("All bank account fields are required");
            redirAttrs.addFlashAttribute("bankAccountNotCompleted", "All bank account fields are required");
            return new ModelAndView(redirectHome);
        }

        // Recherche compte bancaire pre-existant
        User userBank = userService.getUserByEmail(principal.getName());
        BankAccount bankAccountToFind = userBank.getBankAccount();

        // Pas de compte bancaire => CREATION
        if (bankAccountToFind == null) {
            BankAccount newBankAccount = new BankAccount(bankname, iban, bic, userBank);
            bankAccountService.addBankAccount(newBankAccount);
            logger.info("New bankAccount saved");
            redirAttrs.addFlashAttribute("bankAccountAdded", "OK");
            return new ModelAndView(redirectHome);
        }

        // compte bancaire existant => UPDATE
        bankAccountToFind.setBankName(bankname);
        bankAccountToFind.setIban(iban);
        bankAccountToFind.setBic(bic);
        bankAccountService.updateBankAccount(userBank.getBankAccount().getBankAccountId(), bankAccountToFind);
        logger.info("BankAccount updated");
        redirAttrs.addFlashAttribute("bankAccountUpdated", "OK");
        return new ModelAndView(redirectHome);
    }

    @PostMapping("/deposit")
    public ModelAndView addmoney(Float depositAmount, Principal principal, RedirectAttributes redirAttrs) {

        //Vérification montant max. dépôt
        if (depositAmount > MAX_AMOUNT_DEPOSIT) {
            logger.error("The maximum deposit amount is € 10,000");
            redirAttrs.addFlashAttribute("errorAmount", "The maximum deposit amount is 10.000 €");
            return new ModelAndView(redirectHome);
        }

        User connectedUser = userService.getUserByEmail(principal.getName());
        BankAccount accountToFind = connectedUser.getBankAccount();
        // Pas de compte bancaire enregistré
        if (accountToFind == null) {
            logger.error("A bank account is required for deposits and withdrawals");
            redirAttrs.addFlashAttribute("errorAmount", "A bank account is required for deposits and withdrawals");
            return new ModelAndView(redirectHome);
        }

        if (depositAmount < 0) {
            redirAttrs.addFlashAttribute("errorAmount", "Negative amount not allowed !");
            logger.warn("Negative amount not allowed");
            return new ModelAndView(redirectHome);
        }
        bankTransactionService.depositMoneyToBalance(connectedUser, depositAmount);
        redirAttrs.addFlashAttribute("balance", connectedUser);
        redirAttrs.addFlashAttribute("transactionSuccess", "Success deposit money to balance !");
        logger.info("Money deposited to balance");
        return new ModelAndView(redirectHome);
    }

    @PostMapping("/withdraw")
    public ModelAndView withdrawmoney(Float withdrawAmount, Principal principal, RedirectAttributes redirAttrs) {

        User connectedUser = userService.getUserByEmail(principal.getName());
        BankAccount accountToFind = connectedUser.getBankAccount();
        // Pas de compte bancaire enregistré
        if (accountToFind == null) {
            logger.error("A bank account is required for deposits and withdrawals");
            redirAttrs.addFlashAttribute("errorAmount", "A bank account is required for deposits and withdrawals");
            return new ModelAndView(redirectHome);
        }

        if (withdrawAmount < 0) {
            redirAttrs.addFlashAttribute("errorAmount", "Negative amount not allowed !");
            logger.warn("Negative amount not allowed");
            return new ModelAndView(redirectHome);
        }

        Float balance = connectedUser.getBalance();

        if (Float.compare(withdrawAmount, balance) > 0) {
            redirAttrs.addFlashAttribute("errorAmount", "Withdrawal greater than available balance !");
            logger.warn("Withdrawal greater than available balance");
            return new ModelAndView(redirectHome);
        }
        bankTransactionService.withdrawMoneyFromBalance(connectedUser, withdrawAmount);
        redirAttrs.addFlashAttribute("balance", connectedUser);
        redirAttrs.addFlashAttribute("transactionSuccess", "Success withdraw money from balance !");
        logger.info("Money withdraw from balance");
        return new ModelAndView(redirectHome);
    }


}
