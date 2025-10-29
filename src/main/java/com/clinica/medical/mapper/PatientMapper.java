package com.clinica.medical.mapper;

import com.clinica.medical.dto.PatientDTO;
import com.clinica.medical.model.Patient;

public class PatientMapper {

    public static PatientDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }

        return PatientDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .cpf(patient.getCpf())
                .phone(patient.getPhone())
                .build();
    }

    public static Patient toEntity(PatientDTO dto) {
        if (dto == null) {
            return null;
        }

        return Patient.builder()
                .id(dto.getId())
                .name(dto.getName())
                .cpf(dto.getCpf())
                .phone(dto.getPhone())
                .build();
    }
}
