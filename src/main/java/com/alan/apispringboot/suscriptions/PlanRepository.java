package com.alan.apispringboot.suscriptions;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Integer>{

  Optional<Plan> findByPlanName(PlanEnum planName);

}
