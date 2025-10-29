package com.clinica.medical.mapper;

import com.clinica.medical.dto.DoctorDTO;
import com.clinica.medical.model.Doctor;

public class DoctorMapper {

    public static DoctorDTO toDTO(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        return DoctorDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialty(doctor.getSpecialty())
                .crm(doctor.getCrm())
                .build();
    }

    public static Doctor toEntity(DoctorDTO dto) {
        if (dto == null) {
            return null;
        }

        return Doctor.builder()
                .id(dto.getId())
                .name(dto.getName())
                .specialty(dto.getSpecialty())
                .crm(dto.getCrm())
                .build();
    }
}
