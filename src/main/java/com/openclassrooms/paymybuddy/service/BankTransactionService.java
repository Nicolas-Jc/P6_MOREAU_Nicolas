package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.BankTransaction;
import com.openclassrooms.paymybuddy.model.BankingMovementType;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankTransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BankTransactionService {

    private static final Logger logger = LogManager.getLogger("BankTransactionService");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;


    @Transactional
    public void depositMoneyToBalance(User user, Float deposit) {
        // ETAPE 1 : Création et enregistrement Transaction
        BankTransaction bankTransaction = new BankTransaction();

        LocalDateTime dateTime = LocalDateTime.now();
        bankTransaction.setTransactionDate(dateTime);
        bankTransaction.setAmount(deposit);
        bankTransaction.setOwner(user);
        bankTransaction.setMovementType(String.valueOf(BankingMovementType.DEPOSIT));
        bankTransaction.setBankAccount(user.getBankAccount());
        bankTransactionRepository.saveAndFlush(bankTransaction);
        logger.info("bank transaction DEPOSIT saved in DBB");

        // ETAPE 2 : Mise à jour Balance
        Float balance = user.getBalance() + deposit;
        user.setBalance(balance);
        userRepository.saveAndFlush(user);
        logger.info("money deposited in the account");

    }

    @Transactional
    public void withdrawMoneyFromBalance(User user, Float withdraw) {
        // ETAPE 1 : Création et enregistrement Transaction
        BankTransaction bankTransaction = new BankTransaction();

        LocalDateTime dateTime = LocalDateTime.now();
        bankTransaction.setTransactionDate(dateTime);
        bankTransaction.setAmount(withdraw);
        bankTransaction.setOwner(user);
        bankTransaction.setMovementType(String.valueOf(BankingMovementType.WITHDRAW));
        bankTransaction.setBankAccount(user.getBankAccount());
        bankTransactionRepository.saveAndFlush(bankTransaction);
        logger.info("bank transaction WITHDRAW saved in DBB");

        // ETAPE 2 : Mise à jour Balance
        Float balance = user.getBalance() - withdraw;
        user.setBalance(balance);
        userRepository.saveAndFlush(user);
        logger.info("money withdrawed from the account");

    }
}