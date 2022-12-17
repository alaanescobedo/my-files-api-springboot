package com.alan.apispringboot.subscription;

import javax.persistence.*;

import com.alan.apispringboot.users.entities.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Data

@Entity
@Table(name = "subscriptions")
public class Subscription {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonBackReference
  private User user;

  @ManyToOne
  @JoinColumn(name = "plan_id", referencedColumnName = "id")
  private Plan plan;
}
