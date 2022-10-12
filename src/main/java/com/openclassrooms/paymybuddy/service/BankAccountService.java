package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private static final Logger logger = LogManager.getLogger("BankAccountService");

    @Transactional
    public void addBankAccount(BankAccount bankAccount) {

        User userToAddBank = bankAccount.getUser();
        userToAddBank.setBankAccount(bankAccount);
        logger.info("Bank account added for user in DDB");
        bankAccountRepository.saveAndFlush(bankAccount);
    }

    @Transactional
    public void updateBankAccount(Integer bankAccountId, BankAccount accountToUpdate) {
        BankAccount bankAccountToFind = getBankAccountById(bankAccountId);

        if (bankAccountToFind != null) {
            bankAccountToFind.setBankName(accountToUpdate.getBankName());
            bankAccountToFind.setIban(accountToUpdate.getIban());
            bankAccountToFind.setBic(accountToUpdate.getBic());

            bankAccountRepository.saveAndFlush(bankAccountToFind);
        } else {
            logger.error("Bankaccount not exists");
        }
        logger.info("Bankaccount updated and saved");
    }

    public BankAccount getBankAccountById(Integer bankId) {
        BankAccount baToFind = bankAccountRepository.getById(bankId);
        logger.info("Bank account exists");
        return baToFind;
    }


}