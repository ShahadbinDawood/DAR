package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseInvoiceStatsDtoOut {
    private List<CategoryStatDtoOut> byCategory;
    private List<PurchaseInvoiceDtoOut> topPurchases;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryStatDtoOut {
        private String category;
        private Double totalAmount;
        private Long count;
    }
}
