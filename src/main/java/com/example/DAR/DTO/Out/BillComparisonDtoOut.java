package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillComparisonDtoOut {
    private String month;
    private Integer consumption;
    private Double amount;
}
