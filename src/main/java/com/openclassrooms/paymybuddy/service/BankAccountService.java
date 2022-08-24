package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private static Logger logger = LogManager.getLogger("BankAccountService");

    // METHODES A PREVOIR
    // addBankAccount
    // updateBankAccount

    /*public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }*/
    /*public BankAccount getBankAccountByUserId(Integer userId) {
        return bankAccountRepository.findBankAccountByUser(userId);
    }*/

    public BankAccount addBankAccount(BankAccount bankAccount) {

        User verifUserToAddBank = bankAccount.getUser();

        // Recherche compte bancaire existant ? sur ce User
        BankAccount userToAddBankAccount = verifUserToAddBank.getBankAccount();

        // Pas de compte bancaire encore créé pour ce User .
        if (userToAddBankAccount == null) {
            BankAccount newBankAccount = new BankAccount();
            newBankAccount.setBankName(bankAccount.getBankName());
            newBankAccount.setIban(bankAccount.getIban());
            newBankAccount.setBic(bankAccount.getBic());
            newBankAccount.setUser(bankAccount.getUser());

            // Enregistrement des informations compte bancaire sur le User
            verifUserToAddBank.setBankAccount(newBankAccount);
            logger.info("Bank account added for user in DDB");
            // Enregistrement + commit en BDD du CpteBancaire
            bankAccountRepository.saveAndFlush(newBankAccount);
            return newBankAccount;
        }
        logger.info("One BankAccount already exists - Not Create");
        return userToAddBankAccount;
    }


}