package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static Logger logger = LogManager.getLogger("UserService");
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
    public User getUserByEmail(String email) {
        logger.info("email to find: {}",email);
        return userRepository.findByEmail(email);
    }

    public User addUser(User user) {
        User verifUserToAdd = getUserByEmail(user.getEmail());
        // Email inexistant en base. Création User possible
        if(verifUserToAdd == null)
        {
            User newUser = new User();
            newUser.setLastname(user.getLastname());
            newUser.setFirstname(user.getFirstname());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setBalance(0.00f);

            // Enregistrement + commit en BDD
            userRepository.saveAndFlush(newUser);
            logger.info("User added DDB");
            return newUser;
        }
        return verifUserToAdd;
    }


}
