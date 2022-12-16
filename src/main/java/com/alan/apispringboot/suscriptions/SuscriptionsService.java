package com.alan.apispringboot.suscriptions;

import com.alan.apispringboot.users.entities.User;

public interface SuscriptionsService {

  public Suscription createFreeSuscription(User user);
  public Suscription createBasicSuscription(User user);
}