package com.example.DAR.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSubscriptionScheduler {

    private final UserSubscriptionService userSubscriptionService;

    @Scheduled(cron = "0 50 13 * * *")
    public void expireOutdatedSubscriptions() {
        userSubscriptionService.expireOutdatedSubscriptions();
    }
}
