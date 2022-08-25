package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserTransaction;
import com.openclassrooms.paymybuddy.repository.UserTransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserTransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTransactionRepository userTransactionRepository;
    private static Logger logger = LogManager.getLogger("UserTransactionService");

    private final LocalDateTime dateTime = LocalDateTime.now();

    private static final float FEE_RATE = 0.005f;


    // METHODES A PREVOIR
    // sendMoney
    // Appel updateBalance dans UserService
    // getTransactionsBySender / Receiver

    public Iterable<UserTransaction> getUserTransactions() {
        return userTransactionRepository.findAll();
    }

    public UserTransaction sendMoney(User sender, String emailReceiver, String description, Float amount) {
        User receiver = userService.getUserByEmail(emailReceiver);
        // Montant des frais
        Float feeAmount = amount * FEE_RATE;
        // Montant envoyé Net de frais
        Float receiveAmount = amount - feeAmount;

        Float senderBalance = sender.getBalance();
        Float receiverBalance = receiver.getBalance();

        if (senderBalance < receiveAmount) {
            logger.info("Balance not enough: {}, sending Amount: {}", senderBalance, amount);
            // Arrêt à prévoir
            return null;
        }

        sender.setBalance(senderBalance - amount);
        receiver.setBalance(receiverBalance + receiveAmount);

        UserTransaction transaction = new UserTransaction(sender, receiver, dateTime, receiveAmount, description, FEE_RATE);
        userTransactionRepository.save(transaction);
        logger.info("Transaction has been saved " + transaction);
        return transaction;
    }
}