package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserTransaction;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {

    List<UserTransaction> getUserTransactionsBySenderOrderByTransactionDateDesc(User sender);

    /**
     * {@inheritDoc}
     */
    List<UserTransaction> getUserTransactionsByReceiverOrderByTransactionDateDesc(User receiver);

}