package com.alan.apispringboot.subscription;

import com.alan.apispringboot.users.entities.User;

public interface SubscriptionService {

  Subscription createFreeSubscription(User user);
}
