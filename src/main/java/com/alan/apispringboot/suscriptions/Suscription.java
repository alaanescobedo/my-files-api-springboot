package com.alan.apispringboot.suscriptions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.alan.apispringboot.users.entities.User;

@Entity
public class Suscription {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  @OneToOne
  private User user;

  @Column(nullable = false)
  @OneToOne
  private Plan plan;
}
