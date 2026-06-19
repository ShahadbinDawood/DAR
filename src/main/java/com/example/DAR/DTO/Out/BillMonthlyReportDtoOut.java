package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillMonthlyReportDtoOut {
    private String type;
    private Integer totalConsumption;
    private Double totalAmount;
    private Integer billCount;
}
