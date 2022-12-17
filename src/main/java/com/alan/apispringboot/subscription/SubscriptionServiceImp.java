package com.alan.apispringboot.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alan.apispringboot.users.entities.User;

@Service
public class SubscriptionServiceImp implements SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImp.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PlanService planService;

    @Override
    public Subscription createFreeSubscription(User user) {
        logger.info("Creating free subscription for user " + user.getUsername());

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        Plan planBasic = planService.getPlanByPlanName(PlanEnum.PLAN_FREE);
        subscription.setPlan(planBasic);
        subscription.setUser(user);

        return subscriptionRepository.save(subscription);
    }

}
