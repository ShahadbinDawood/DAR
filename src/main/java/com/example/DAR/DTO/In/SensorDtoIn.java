package com.example.DAR.DTO.In;

import com.example.DAR.Model.Home;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SensorDtoIn {


    @NotEmpty(message = "type is required")
    private String type;
    @NotEmpty(message = "location is required")
    private String location;
    @NotBlank(message = "Serial number is required")
    @Pattern(
            regexp = "^[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$",
            message = "Serial number must match pattern XXXX-XXXX-XXXX (uppercase letters and numbers only)"
    )
    private String  serialNumber;





}
