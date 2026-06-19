package com.example.DAR.Service;

import com.example.DAR.Model.Bill;
import com.example.DAR.Repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BillReminderScheduler {

    private final BillRepository billRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * *") // كل يوم 9 الصبح
    public void sendDueDateReminders() {
        LocalDate today = LocalDate.now();
        LocalDate threeDaysLater = today.plusDays(3);

        List<Bill> upcomingBills = billRepository.findByDueDateBetweenAndStatus(today, threeDaysLater, "PENDING");

        for (Bill bill : upcomingBills) {
            String typeAr = switch (bill.getType().toUpperCase()) {
                case "ELECTRICITY" -> "الكهرباء";
                case "WATER" -> "الماء";
                case "GAS" -> "الغاز";
                default -> bill.getType();
            };
            long daysLeft = today.until(bill.getDueDate()).getDays();
            String message = "فاتورة " + typeAr + " تستحق خلال " + daysLeft + " يوم(أيام). لا تنسَ السداد لتجنب الغرامات.";
            notificationService.sendBillDueNotification(bill.getHome().getUser(), typeAr, message);
        }
    }
}
