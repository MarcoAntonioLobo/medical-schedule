package com.clinica.medical.mapper;


import org.springframework.stereotype.Component;

import com.clinica.medical.dto.AppointmentCreateDTO;
import com.clinica.medical.dto.AppointmentResponseDTO;
import com.clinica.medical.model.Appointment;
import com.clinica.medical.model.Doctor;
import com.clinica.medical.model.Patient;

@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentCreateDTO dto, Doctor doctor, Patient patient) {
        return Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .dateTime(dto.getDateTime())
                .notes(dto.getNotes())
                .build();
    }

    public AppointmentResponseDTO toResponse(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .doctorName(appointment.getDoctor().getName())
                .patientName(appointment.getPatient().getName())
                .dateTime(appointment.getDateTime())
                .notes(appointment.getNotes())
                .build();
    }
}