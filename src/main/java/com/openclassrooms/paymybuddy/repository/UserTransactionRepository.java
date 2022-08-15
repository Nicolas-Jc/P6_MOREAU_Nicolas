package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.model.UserTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTransactionRepository extends CrudRepository<UserTransaction, Integer> {

}