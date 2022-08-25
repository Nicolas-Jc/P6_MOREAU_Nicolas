package com.openclassrooms.paymybuddy.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private Integer bankAccountId;

    @Column(name = "bankname")
    private String bankName;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bic")
    private String bic;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public BankAccount() {
    }

    public BankAccount(Integer bankAccountId, String bankName, String iban, String bic, User user) {
        this.bankAccountId = bankAccountId;
        this.bankName = bankName;
        this.iban = iban;
        this.bic = bic;
        this.user = user;
    }

    // Constructeur spécifique
    public BankAccount(String bankName, String iban, String bic, User user) {
        this.bankName = bankName;
        this.iban = iban;
        this.bic = bic;
        this.user = user;
    }

    public BankAccount(String newBankName, String newIban, String newbic) {
        this.bankName = newBankName;
        this.iban = newIban;
        this.bic = newbic;
    }


    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
