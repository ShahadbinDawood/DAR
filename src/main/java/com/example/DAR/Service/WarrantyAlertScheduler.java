package com.example.DAR.Service;

import com.example.DAR.Model.PurchaseInvoice;
import com.example.DAR.Repository.PurchaseInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WarrantyAlertScheduler {

    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 10 * * *") // كل يوم 10 الصبح
    public void sendWarrantyExpiryAlerts() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);

        List<PurchaseInvoice> expiring = purchaseInvoiceRepository.findByWarrantyExpiryBetween(today, sevenDaysLater);

        for (PurchaseInvoice invoice : expiring) {
            if (invoice.getHome() == null || invoice.getHome().getUser() == null) continue;

            long daysLeft = today.until(invoice.getWarrantyExpiry()).getDays();
            String message = "ضمان المنتج \"" + invoice.getProductName() + "\" المشترى من " +
                    invoice.getStore() + " سينتهي خلال " + daysLeft + " يوم(أيام).\n" +
                    (invoice.getWarrantyNote() != null ? "ملاحظة الضمان: " + invoice.getWarrantyNote() : "");

            notificationService.sendWarrantyExpiryNotification(invoice.getHome().getUser(), invoice.getProductName(), message);
        }
    }
}
