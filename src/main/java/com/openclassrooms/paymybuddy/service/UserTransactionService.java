package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserTransaction;
import com.openclassrooms.paymybuddy.repository.UserTransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserTransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTransactionRepository userTransactionRepository;
    private static Logger logger = LogManager.getLogger("UserTransactionService");

    private static final float FEE_RATE = 0.005f;


    public Iterable<UserTransaction> getUserTransactions() {
        return userTransactionRepository.findAll();
    }

    public List<UserTransaction> getTransacsBySender(User sender) {
        logger.info("Transactions founded for: {}", sender);
        return userTransactionRepository.getUserTransactionsBySenderOrderByTransactionDateDesc(sender);
    }

    public List<UserTransaction> getTransacsByReceiver(User receiver) {
        logger.info("Transactions founded for: {}", receiver);
        return userTransactionRepository.getUserTransactionsByReceiverOrderByTransactionDateDesc(receiver);
    }

    public void sendMoney(User sender, String emailReceiver, String description, Float amount) {
        User receiver = userService.getUserByEmail(emailReceiver);
        // Montant des frais
        Float feeAmount = amount * FEE_RATE;
        // Montant envoyé Net de frais
        Float receiveAmount = amount - feeAmount;

        Float senderBalance = sender.getBalance();
        Float receiverBalance = receiver.getBalance();

        // Mise à jour des soldes de l'expéditeur et destinataire
        sender.setBalance(senderBalance - amount);
        receiver.setBalance(receiverBalance + receiveAmount);

        LocalDateTime dateTime = LocalDateTime.now();

        UserTransaction transaction = new UserTransaction(sender, receiver, dateTime, receiveAmount, description, feeAmount);
        userTransactionRepository.save(transaction);
        logger.info("Transaction has been saved: {}", transaction);
    }
}