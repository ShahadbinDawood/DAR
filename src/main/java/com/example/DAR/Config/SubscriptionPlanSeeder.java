package com.example.DAR.Config;

import com.example.DAR.Model.SubscriptionPlan;
import com.example.DAR.Repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionPlanSeeder implements CommandLineRunner {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public void run(String... args) {
        savePlan("Standard", "Good choice for organized home management", 50.0, false, 3, 50, 20, 50, 20, true, false);
        savePlan("Professional", "Best choice for villas and smart homes", 200.0, true, 10, 200, 100, 200, 100, true, true);
    }

    private void savePlan(String name, String subtitle, Double price, Boolean isPopular, Integer maxHomes,
                          Integer maxItems, Integer maxSensors, Integer maxNotificationsPerMonth,
                          Integer maxAiReportsPerMonth, Boolean weatherReminderEnabled,
                          Boolean dailyAIReminder) {
        SubscriptionPlan plan = subscriptionPlanRepository.findSubscriptionPlanByName(name);
        if (plan == null) {
            plan = new SubscriptionPlan();
        }
        plan.setName(name);
        plan.setSubtitle(subtitle);
        plan.setPrice(price);
        plan.setIsPopular(isPopular);
        plan.setMaxHomes(maxHomes);
        plan.setMaxItems(maxItems);
        plan.setMaxSensors(maxSensors);
        plan.setMaxNotificationsPerMonth(maxNotificationsPerMonth);
        plan.setMaxAiReportsPerMonth(maxAiReportsPerMonth);
        plan.setWeatherReminderEnabled(weatherReminderEnabled);
        plan.setDailyAIReminder(dailyAIReminder);

        subscriptionPlanRepository.save(plan);
    }
}
