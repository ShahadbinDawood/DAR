package com.example.DAR.DTO.In;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionPlanDtoIn {
    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Subtitle is required")
    private String subtitle;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @NotNull(message = "Popular flag is required")
    private Boolean isPopular;

    @NotNull(message = "Contact sales flag is required")
    private Boolean contactSales;

    @NotNull(message = "Max homes is required")
    @Positive(message = "Max homes must be positive")
    private Integer maxHomes;

    @NotNull(message = "Max items is required")
    @Positive(message = "Max items must be positive")
    private Integer maxItems;

    @NotNull(message = "Max sensors is required")
    @Positive(message = "Max sensors must be positive")
    private Integer maxSensors;

    @NotNull(message = "Max notifications per month is required")
    @PositiveOrZero(message = "Max notifications must be zero or positive")
    private Integer maxNotificationsPerMonth;

    @NotNull(message = "Max AI reports per month is required")
    @PositiveOrZero(message = "Max AI reports must be zero or positive")
    private Integer maxAiReportsPerMonth;

    @NotNull(message = "Weather reminder enabled is required")
    private Boolean weatherReminderEnabled;

    @NotNull(message = "Usage spike detection enabled is required")
    private Boolean usageSpikeDetectionEnabled;

    @NotNull(message = "Priority support enabled is required")
    private Boolean prioritySupportEnabled;
}
