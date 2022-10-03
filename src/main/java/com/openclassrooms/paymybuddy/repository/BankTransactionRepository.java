package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.BankTransaction;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransaction, Integer> {

}