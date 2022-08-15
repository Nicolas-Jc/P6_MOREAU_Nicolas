package com.openclassrooms.paymybuddy.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@Table(name ="bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bank_account_id")
    private Integer bankAccountId;

    @Column(name="bankname")
    private String bankName;

    @Column(name="iban")
    private String iban;

    @Column(name="bic")
    private String bic;

    /*@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;*/

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

}
