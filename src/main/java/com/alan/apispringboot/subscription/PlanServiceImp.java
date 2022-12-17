package com.alan.apispringboot.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alan.apispringboot.exceptions.NotFoundException;

@Service
public class PlanServiceImp implements PlanService {

  private static final Logger logger = LoggerFactory.getLogger(PlanServiceImp.class);

  @Autowired
  private PlanRepository planRepository;

  public Plan getPlanByPlanName(PlanEnum planName) {
    logger.info("Getting plan {}", planName);

    return planRepository.findByPlanName(planName)
        .orElseThrow(() -> new NotFoundException(PlanEnum.PLAN_BASIC.toString()));
  }

}
