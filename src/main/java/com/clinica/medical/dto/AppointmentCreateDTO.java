package com.clinica.medical.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentCreateDTO {

    @NotNull(message = "Doctor ID is mandatory")
    private Long doctorId;

    @NotNull(message = "Patient ID is mandatory")
    private Long patientId;

    @NotNull(message = "Date and time are mandatory")
    private LocalDateTime dateTime;

    private String notes;
}
