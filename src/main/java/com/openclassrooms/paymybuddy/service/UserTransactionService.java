package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserTransaction;
import com.openclassrooms.paymybuddy.model.paging.Page;
import com.openclassrooms.paymybuddy.model.paging.Paged;
import com.openclassrooms.paymybuddy.model.paging.Paging;
import com.openclassrooms.paymybuddy.repository.UserTransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTransactionRepository userTransactionRepository;
    private static final Logger logger = LogManager.getLogger("UserTransactionService");

    private static final float FEE_RATE = 0.005f;

    public Paged<UserTransaction> getTransacsBySenderPaginated(User sender, int pageNumber, int size) {

        List<UserTransaction> transactionsSentList = userTransactionRepository.getUserTransactionsBySenderOrderByTransactionDateDesc(sender);
        logger.info("TransactionsSentListPaginated founded for: {}", sender);

        return getTransacsPaginated(transactionsSentList, pageNumber, size);
    }

    public Paged<UserTransaction> getTransacsByReceiverPaginated(User sender, int pageNumber, int size) {

        List<UserTransaction> transactionsReceivedList = userTransactionRepository.getUserTransactionsByReceiverOrderByTransactionDateDesc(sender);
        logger.info("TransactionsReceivedListPaginated founded for: {}", sender);

        return getTransacsPaginated(transactionsReceivedList, pageNumber, size);
    }

    public Paged<UserTransaction> getTransacsPaginated(List<UserTransaction> transactionsList, int pageNumber, int size) {

        int totalPages = ((transactionsList.size() - 1) / size) + 1;
        int skip = pageNumber > 1 ? (pageNumber - 1) * size : 0;

        List<UserTransaction> paged = transactionsList.stream()
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());

        return new Paged<>(new Page<>(paged, totalPages), Paging.of(totalPages, pageNumber, size));
    }

    @Transactional
    public void sendMoney(User sender, String emailReceiver, String description, Float amount) {
        User receiver = userService.getUserByEmail(emailReceiver);

        float feeAmount = amount * FEE_RATE;
        Float receiveAmount = amount - feeAmount;

        Float senderBalance = sender.getBalance();
        Float receiverBalance = receiver.getBalance();

        sender.setBalance(senderBalance - amount);
        receiver.setBalance(receiverBalance + receiveAmount);

        LocalDateTime dateTime = LocalDateTime.now();

        UserTransaction transaction = new UserTransaction(sender, receiver, dateTime, receiveAmount, description, feeAmount);
        userTransactionRepository.save(transaction);
        logger.info("Transaction has been saved: {}", transaction);
    }
}