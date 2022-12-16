package com.alan.apispringboot.suscriptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alan.apispringboot.users.entities.User;

@Service
public class SuscriptionsServiceImp implements SuscriptionsService {

    private static final Logger logger = LoggerFactory.getLogger(SuscriptionsServiceImp.class);

    @Autowired
    private SuscriptionsRepository suscriptionsRepository;

    @Autowired
    private PlanService planService;

    @Override
    public Suscription createFreeSuscription(User user) {
        logger.info("Creating free suscription for user " + user.getUsername());

        Suscription suscription = new Suscription();
        suscription.setUser(user);
        Plan planBasic = planService.getPlanByPlanName(PlanEnum.PLAN_FREE);
        suscription.setPlan(planBasic);
        suscription.setUser(user);

        return suscriptionsRepository.save(suscription);
    }

    @Override
    public Suscription createBasicSuscription(User user) {
        logger.info("Creating basic suscription");

        Suscription suscription = new Suscription();
        Plan planBasic = planService.getPlanByPlanName(PlanEnum.PLAN_BASIC);
        suscription.setPlan(planBasic);
        suscription.setUser(user);

        return suscriptionsRepository.save(suscription);
    }

}
