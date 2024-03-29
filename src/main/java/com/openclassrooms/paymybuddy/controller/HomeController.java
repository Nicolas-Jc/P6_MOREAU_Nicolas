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
    private static final String REDIRECT_HOME = "redirect:/home";
    private static final String BALANCE_ATTR = "balance";
    private static final String ERROR_AMOUNT_ATTR = "errorAmount";


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

        if (accountToFind == null) {
            model.addAttribute("user", connectedUser);
            logger.warn("No bank account recorded");
            return "home";
        }
        logger.info("Loading User Information");
        model.addAttribute("user", connectedUser);
        model.addAttribute("bankAccount", accountToFind);

        Float balance = connectedUser.getBalance();
        model.addAttribute(BALANCE_ATTR, balance);
        return "home";
    }

    @PostMapping("/addBankAccount")
    public ModelAndView addbank(String bankname, String iban, String bic, Principal principal, RedirectAttributes redirAttrs) {

        if (bankname.isEmpty() || iban.isEmpty() || bic.isEmpty()) {
            logger.warn("All bank account fields are required");
            redirAttrs.addFlashAttribute("bankAccountNotCompleted", "All bank account fields are required");
            return new ModelAndView(REDIRECT_HOME);
        }

        User userBank = userService.getUserByEmail(principal.getName());
        BankAccount bankAccountToFind = userBank.getBankAccount();

        if (bankAccountToFind == null) {
            BankAccount newBankAccount = new BankAccount(bankname, iban, bic, userBank);
            bankAccountService.addBankAccount(newBankAccount);
            logger.info("New bankAccount saved");
            redirAttrs.addFlashAttribute("bankAccountAdded", "OK");
            return new ModelAndView(REDIRECT_HOME);
        }

        bankAccountToFind.setBankName(bankname);
        bankAccountToFind.setIban(iban);
        bankAccountToFind.setBic(bic);
        bankAccountService.updateBankAccount(userBank.getBankAccount().getBankAccountId(), bankAccountToFind);
        logger.info("BankAccount updated");
        redirAttrs.addFlashAttribute("bankAccountUpdated", "OK");
        return new ModelAndView(REDIRECT_HOME);
    }

    @PostMapping("/deposit")
    public ModelAndView addmoney(String depositAmount, Principal principal, RedirectAttributes redirAttrs) {

        User connectedUser = userService.getUserByEmail(principal.getName());
        BankAccount accountToFind = connectedUser.getBankAccount();
        if (accountToFind == null) {
            logger.error("Bank account required for deposits and withdrawals");
            redirAttrs.addFlashAttribute(ERROR_AMOUNT_ATTR, "A bank account is required for deposits");
            return new ModelAndView(REDIRECT_HOME);
        }

        try {
            float depositToCheck = Float.parseFloat(depositAmount);

            if (depositToCheck == 0) {
                return new ModelAndView(REDIRECT_HOME);
            }

            if (depositToCheck > MAX_AMOUNT_DEPOSIT) {
                logger.error("The maximum deposit amount is € 10,000");
                redirAttrs.addFlashAttribute(ERROR_AMOUNT_ATTR, "The maximum deposit amount is 10.000 €");
                return new ModelAndView(REDIRECT_HOME);
            }

            if (depositToCheck < 0) {
                redirAttrs.addFlashAttribute(ERROR_AMOUNT_ATTR, "Negative amount not allowed !");
                logger.warn("Negative amount not allowed");
                return new ModelAndView(REDIRECT_HOME);
            }

            bankTransactionService.depositMoneyToBalance(connectedUser, depositToCheck);
            redirAttrs.addFlashAttribute(BALANCE_ATTR, connectedUser);
            redirAttrs.addFlashAttribute("transactionSuccess", "Success deposit money to balance !");
            logger.info("Money deposited to balance");
            return new ModelAndView(REDIRECT_HOME);

        } catch (NumberFormatException e) {
            redirAttrs.addFlashAttribute(ERROR_AMOUNT_ATTR, "Only numerical values are allowed");
            return new ModelAndView(REDIRECT_HOME);
        }
    }

    @PostMapping("/withdraw")
    public ModelAndView withdrawmoney(String withdrawAmount, Principal principal, RedirectAttributes redirAttrs) {

        User connectedUser = userService.getUserByEmail(principal.getName());
        BankAccount accountToFind = connectedUser.getBankAccount();

        if (accountToFind == null) {
            logger.error("Bank account required for deposits - withdrawals");
            redirAttrs.addFlashAttribute(ERROR_AMOUNT_ATTR, "A bank account is required for withdrawals");
            return new ModelAndView(REDIRECT_HOME);
        }

        try {
            float withdrawToCheck = Float.parseFloat(withdrawAmount);

            if (withdrawToCheck == 0) {
                return new ModelAndView(REDIRECT_HOME);
            }

            if (withdrawToCheck < 0) {
                redirAttrs.addFlashAttribute(ERROR_AMOUNT_ATTR, "Negative amount not allowed !");
                logger.warn("Negative amount not allowed");
                return new ModelAndView(REDIRECT_HOME);
            }

            Float balance = connectedUser.getBalance();

            if (Float.compare(withdrawToCheck, balance) > 0) {
                redirAttrs.addFlashAttribute(ERROR_AMOUNT_ATTR, "Withdrawal greater than available balance !");
                logger.warn("Withdrawal greater than available balance");
                return new ModelAndView(REDIRECT_HOME);
            }
            bankTransactionService.withdrawMoneyFromBalance(connectedUser, withdrawToCheck);
            redirAttrs.addFlashAttribute(BALANCE_ATTR, connectedUser);
            redirAttrs.addFlashAttribute("transactionSuccess", "Success withdraw money from balance !");
            logger.info("Money withdraw from balance");
            return new ModelAndView(REDIRECT_HOME);

        } catch (NumberFormatException e) {
            redirAttrs.addFlashAttribute(ERROR_AMOUNT_ATTR, "Only numerical values are allowed");
            return new ModelAndView(REDIRECT_HOME);
        }
    }

}
