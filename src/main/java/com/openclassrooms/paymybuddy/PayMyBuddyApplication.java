package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Transactional
public class PayMyBuddyApplication implements CommandLineRunner {

    private static Logger logger = (Logger) LogManager.getLogger("PayMyBuddy");

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
        //logger.info("Démarrage PayMyBuddy");
    }
    @Override
    public void run(String... args) throws Exception {

        List<User> contactsListTest;
        contactsListTest = new ArrayList<>();

        User user = new User("NomUserCodeAdded1","PrenomCodeAddedUser1","mailuser1CodeAdded@gmail.com","123456", 0.00f,contactsListTest);
        User userToAdd = userService.addUser(user);


        logger.info("Récupération email users en BDD");
        // Récupération Liste de tous les email de tous les users en BDD
        Iterable<User> users = userService.getUsers();
        users.forEach(user2 -> System.out.println(user2.getEmail()));

        /*logger.info("Requête sur un email existant");
        User user = userService.getUserByEmail("mailtest2@gmail.com");
        System.out.println(user.getLastname());*/
    }
}
