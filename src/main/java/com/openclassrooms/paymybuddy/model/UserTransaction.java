package com.openclassrooms.paymybuddy.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@Table(name ="user_transaction")
public class UserTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_transaction_id")
    private Integer userTransactionId;

    @Column(name="amount")
    private Float amount;

    @Column(name="fee")
    private Float fee;

    @Column(name="transaction_date")
    private LocalDate transactionDate;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

}
