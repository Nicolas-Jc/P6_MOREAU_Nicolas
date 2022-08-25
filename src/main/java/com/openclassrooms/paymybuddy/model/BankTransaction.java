package com.openclassrooms.paymybuddy.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@Table(name = "bank_transaction")
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_transaction_id")
    private Integer bankTransactionId;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "movement_Type")
    private String movementType;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private User owner;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    public BankTransaction() {
    }

    public BankTransaction(Integer bankTransactionId, Float amount, LocalDateTime transactionDate, String movementType, User owner, BankAccount bankAccount) {
        this.bankTransactionId = bankTransactionId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.movementType = movementType;
        this.owner = owner;
        this.bankAccount = bankAccount;
    }

    public Integer getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(Integer bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }


    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User receiver) {
        this.owner = receiver;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
