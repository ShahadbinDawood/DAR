package com.example.DAR.Repository;

import com.example.DAR.Model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {
    SubscriptionPlan findSubscriptionPlanById(Integer id);
    SubscriptionPlan findSubscriptionPlanByName(String name);
}
