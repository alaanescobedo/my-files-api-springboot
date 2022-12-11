package com.alan.apispringboot.users.entities;

import lombok.Data;

import javax.persistence.*;

@Data

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String country;

    @OneToOne
    private User user;

}
