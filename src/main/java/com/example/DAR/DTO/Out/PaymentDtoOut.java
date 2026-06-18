package com.example.DAR.DTO.Out;

import com.example.DAR.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDtoOut {
    private Integer id;
    private Integer userSubscriptionId;
    private Double amount;
    private String paymentMethod;
    private LocalDate paymentDate;
    private PaymentStatus status;
    private String transactionReference;
}
