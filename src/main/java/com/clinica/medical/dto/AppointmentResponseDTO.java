package com.clinica.medical.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {
    private Long id;

    @NotBlank
    private String doctorName;

    @NotBlank
    private String patientName;

    @NotNull
    private LocalDateTime dateTime;

    private String notes;
}
