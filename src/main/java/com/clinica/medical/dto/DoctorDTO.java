package com.clinica.medical.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String specialty;

    @NotBlank
    private String crm;
}
