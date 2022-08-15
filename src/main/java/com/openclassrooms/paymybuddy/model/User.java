package com.openclassrooms.paymybuddy.model;

import com.sun.istack.NotNull;
import org.hibernate.annotations.DynamicUpdate;

import javax.annotation.Generated;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name ="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name="lastname")
    private String lastname;

    @Column(name="firstname")
    private String firstname;

    @Column(name="email", nullable = false,unique =true)
    @NotNull
    private String email;

    @Column(name="password")
    @NotNull
    private String password;

    @Column(name="balance")
    private Float balance;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    List<UserTransaction> userTransactions = new ArrayList<>();


}
