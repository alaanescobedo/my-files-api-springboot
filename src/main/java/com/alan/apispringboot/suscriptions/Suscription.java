package com.alan.apispringboot.suscriptions;

import javax.persistence.*;

import com.alan.apispringboot.users.entities.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data

@Entity
@Table(name = "suscriptions")
public class Suscription {
  
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
