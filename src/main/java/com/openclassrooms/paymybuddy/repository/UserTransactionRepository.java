package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.UserTransaction;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {

}