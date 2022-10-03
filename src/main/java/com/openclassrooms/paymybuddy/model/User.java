package com.openclassrooms.paymybuddy.model;

//import com.sun.istack.NotNull;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "balance")
    private Float balance;

    @OneToOne
    @JoinColumn(name = "bank_account_Id", referencedColumnName = "bank_account_id")
    private BankAccount bankAccount;

    /*@OneToMany(
            // Toute action sur User sera propagée sur UserTransaction
            cascade = CascadeType.ALL,
            // Si on supprime une userTransaction de la liste, elle est supprimée de la BDD
            orphanRemoval = true,
            // à la récupération du User, toutes les userTransaction sont récupérées
            fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    List<UserTransaction> userTransactions = new ArrayList<>();
    */

    // LAZY => à la récupération du USer, la liste des contacts n'est pas récupérée
    // meilleures performances
    //@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)

    /*@OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "connection", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_contact_id"))*/
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "connection",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_contact_id", referencedColumnName = "user_id"))
    private List<User> contactsList = new ArrayList<>();

    // A la récupération du User, toutes les userTransactions sont récupérées
    /*@OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<UserTransaction> userTransactions = new ArrayList<>();*/


    public User() {
    }

    public User(Integer userId, String lastname, String firstname, String email, String password, Float balance, List<UserTransaction> userTransactions, List<User> contactsList) {
        this.userId = userId;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.balance = balance;
        //this.userTransactions = userTransactions;
        this.contactsList = contactsList;
    }

    // Constructeur spécifique
    public User(String lastname, String firstname, String email, String password, Float balance, List<User> contactsList) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.contactsList = contactsList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    /*public List<UserTransaction> getUserTransactions() {
        return userTransactions;
    }*/

    /*public void setUserTransactions(List<UserTransaction> userTransactions) {
        this.userTransactions = userTransactions;
    }*/

    public List<User> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<User> contactsList) {
        this.contactsList = contactsList;
    }

    public void addUserContact(User contact) {
        contactsList.add(contact);
    }

    public void removeUserContact(User contactToRemove) {
        contactsList.remove(contactToRemove);
    }
}
