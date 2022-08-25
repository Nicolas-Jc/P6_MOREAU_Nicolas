package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.service.UserTransactionService;
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

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserTransactionService userTransactionService;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
        //logger.info("Démarrage PayMyBuddy");
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("Création 2 Users en BDD");
        List<User> contactsListTest;
        contactsListTest = new ArrayList<>();
        User user1 = new User("NomUserCodeAdded1", "PrenomCodeAddedUser1", "mailuser1CodeAdded@gmail.com", "123456", 0.00f, contactsListTest);
        User user2 = new User("NomUserCodeAdded2", "PrenomCodeAddedUser2", "mailuser2CodeAdded@gmail.com", "123456", 0.00f, contactsListTest);
        User contactToAdd = new User("NomContactToAdd", "PrenomContactToAdd", "mailContactToAdd@gmail.com", "123456", 0.00f, contactsListTest);

        //User userToAdd1 = userService.addUser(user1);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(contactToAdd);

        //User userToAdd2 = userService.addUser(user2);

        // -----------------------------------------------------
        logger.info("Récupération email users en BDD");
        // Récupération Liste de tous les email en BDD
        Iterable<User> users = userService.getUsers();
        users.forEach(user3 -> System.out.println(user3.getEmail()));
        // -----------------------------------------------------
        logger.info("Requête sur un email existant");
        User userEmailToFindAndChange = userService.getUserByEmail("mailuser2CodeAdded@gmail.com");
        System.out.println(userEmailToFindAndChange.getLastname());
        // -------------------------------------------------------------
        logger.info("Mise à jour NOM DE FAMILLE et PASSWORD user mailuser2CodeAdded@gmail.com");
        String newLastName = "NEWLASTNAME";
        String newPassword = "999999999";
        userService.updateUser(userEmailToFindAndChange.getEmail(),
                newLastName,
                userEmailToFindAndChange.getFirstname(),
                newPassword);
        System.out.println(userEmailToFindAndChange.getEmail());
        // -------------------------------------------------------------
        logger.info("Tentative création User avec Mail existant");
        User userExistToCreate = new User("NomUserCodeAdded1", "PrenomCodeAddedUser1", "mailuser1CodeAdded@gmail.com", "123456", 0.00f, contactsListTest);
        userService.addUser(userExistToCreate);
        // -----------------------------------------------------------
        logger.info("Création Compte bancaire sur User1");
        User userToAddBankAccount = userService.getUserByEmail("mailuser1CodeAdded@gmail.com");
        BankAccount bankAccount1 = new BankAccount("BankName", "Iban", "Bic", userToAddBankAccount);
        //User userToAdd1 = userService.addUser(user1);
        bankAccountService.addBankAccount(bankAccount1);
        // -------------------------------------------------
        logger.info("Mise à jour COMPTE BANCAIRE user mailuser1CodeAdded@gmail.com");
        // Requête sur User1
        User userBankAccountToChange = userService.getUserByEmail("mailuser1CodeAdded@gmail.com");
        String newBankName = "NEW BANK NAME";
        String newIban = "NEW IBAN XXXXXXXX";
        String newbic = "NEW BIC ZZZZZZZZZZ";

        BankAccount newBankAccount = new BankAccount(newBankName, newIban, newbic);
        //System.out.println(user1.getBankAccount().getBankAccountId());
        //bankAccountService.updateBankAccount(user1.getBankAccount().getBankAccountId(), newBankAccount);
        bankAccountService.updateBankAccount(userBankAccountToChange.getBankAccount().getBankAccountId(), newBankAccount);

        // -------------------------------------------------
        logger.info("ADD CONTACT TO USER1");
        // ATTENTION BUG - CREATION DE DOUBLON DANS USER !!!!!!
        // Requête sur User1
        User userToAddContact = userService.getUserByEmail("mailuser1CodeAdded@gmail.com");
        userService.addContact(userToAddContact, contactToAdd);

        // -------------------------------------------------
        logger.info("SEND MONEY FROM USER1 TO USER2");
        // Alimentation du solde du USER 1
        User user1Sender = userService.getUserByEmail("mailuser1CodeAdded@gmail.com");
        userService.updateBalance("mailuser1CodeAdded@gmail.com", 1000.00f);
        userTransactionService.sendMoney(user1Sender,
                "mailuser2CodeAdded@gmail.com",
                "Test_Send_Money",
                20.00f);
    }
}
