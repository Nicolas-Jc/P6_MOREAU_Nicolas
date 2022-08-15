package com.openclassrooms.paymybuddy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;

@SpringBootApplication
@Transactional
public class PayMyBuddyApplication {

    private static Logger loggerMain = LogManager.getLogger("PayMyBuddyApplication");

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
        loggerMain.info("Démarrage PayMyBuddy");
    }

}
