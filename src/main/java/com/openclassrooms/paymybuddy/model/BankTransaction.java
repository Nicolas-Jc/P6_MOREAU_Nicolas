package com.openclassrooms.paymybuddy.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@Table(name ="bank_transaction")
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bank_transaction_id")
    private Integer bankTransactionId;

    @Column(name="amount")
    private Float amount;

    @Column(name="transaction_date")
    private LocalDate transactionDate;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private User receiver;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="bank_account_id")
    private BankAccount bankAccount;



}
