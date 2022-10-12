package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger("UserService");
    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Transactional
    public User addUser(User user) {
        User verifUserToAdd = getUserByEmail(user.getEmail());
        if (verifUserToAdd == null) {
            User newUser = new User();
            newUser.setLastname(user.getLastname());
            newUser.setFirstname(user.getFirstname());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            newUser.setBalance(0.00f);

            userRepository.saveAndFlush(newUser);
            logger.info("User added DDB");
            return newUser;
        }
        logger.error("User exists Not Create");
        return verifUserToAdd;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User updateUser(String userEmail, String lastName, String firstName, String password) {
        User userToUpdate = userRepository.findByEmail(userEmail);

        if (userToUpdate.getEmail().equals(userEmail)) {
            logger.info("userToUpdate : {}", userToUpdate);
            userToUpdate.setLastname(lastName);
            userToUpdate.setFirstname(firstName);
            userToUpdate.setPassword(password);
            userRepository.saveAndFlush(userToUpdate);
        } else {
            logger.error("User not exists");
        }
        logger.info("User updated and saved");
        return userToUpdate;
    }

    @Transactional
    public void addContact(User user, User contactToAdd) {
        user.addUserContact(contactToAdd);
        userRepository.saveAndFlush(user);
        logger.info("User added to connection");
    }

    public Boolean verifNewUser(User user) {
        User userToFind = getUserByEmail(user.getEmail());
        return userToFind != null;
    }

}
