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
public class PatientCreateDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "CPF is mandatory")
    private String cpf;

    @NotBlank(message = "Phone is mandatory")
    private String phone;
}
