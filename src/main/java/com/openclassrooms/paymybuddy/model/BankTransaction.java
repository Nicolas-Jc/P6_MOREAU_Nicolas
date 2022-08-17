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

    @Column(name="movement_Type")
    private String movementType;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private User receiver;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="bank_account_id")
    private BankAccount bankAccount;

    public BankTransaction()
    {}
    public BankTransaction(Integer bankTransactionId, Float amount, LocalDate transactionDate, String description, String movementType, User receiver, BankAccount bankAccount) {
        this.bankTransactionId = bankTransactionId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
        this.movementType = movementType;
        this.receiver = receiver;
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
