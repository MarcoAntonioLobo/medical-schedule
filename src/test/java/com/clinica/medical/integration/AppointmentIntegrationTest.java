package com.clinica.medical.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.clinica.medical.dto.AppointmentCreateDTO;
import com.clinica.medical.dto.AppointmentResponseDTO;
import com.clinica.medical.exceptions.DuplicateResourceException;
import com.clinica.medical.exceptions.EntityNotFoundException;
import com.clinica.medical.model.Doctor;
import com.clinica.medical.model.Patient;
import com.clinica.medical.repository.DoctorRepository;
import com.clinica.medical.repository.PatientRepository;
import com.clinica.medical.service.AppointmentService;

@SpringBootTest
@ActiveProfiles("test")
public class AppointmentIntegrationTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        doctorRepository.deleteAll();
        patientRepository.deleteAll();

        doctor = doctorRepository.save(Doctor.builder()
                .name("Dr. Test")
                .specialty("Cardiology")
                .crm("CRM123")
                .build());

        patient = patientRepository.save(Patient.builder()
                .name("Patient Test")
                .cpf("111.222.333-44")
                .phone("99999-0000")
                .build());
    }

    @Test
    void fullAppointmentLifecycle() {
        // USANDO HORÁRIOS FIXOS PARA CONSISTÊNCIA
        LocalDateTime appointmentTime1 = LocalDateTime.of(2025, 10, 30, 10, 0);
        LocalDateTime appointmentTime2 = LocalDateTime.of(2025, 10, 30, 11, 0);

        // CREATE
        AppointmentCreateDTO createDto = new AppointmentCreateDTO(doctor.getId(), patient.getId(), appointmentTime1, "Checkup");
        AppointmentResponseDTO created = appointmentService.create(createDto);
        assertThat(created).isNotNull();
        assertThat(created.getDoctorName()).isEqualTo(doctor.getName());
        assertThat(created.getDateTime()).isEqualTo(appointmentTime1);

        // GET BY ID
        AppointmentResponseDTO found = appointmentService.getById(created.getId());
        assertThat(found.getId()).isEqualTo(created.getId());

        // GET ALL
        List<AppointmentResponseDTO> all = appointmentService.getAll();
        assertThat(all).hasSize(1);

        // UPDATE
        AppointmentCreateDTO updateDto = new AppointmentCreateDTO(doctor.getId(), patient.getId(), appointmentTime2, "Updated notes");
        AppointmentResponseDTO updated = appointmentService.update(created.getId(), updateDto);
        assertThat(updated.getNotes()).isEqualTo("Updated notes");
        assertThat(updated.getDateTime()).isEqualTo(appointmentTime2);

        // DUPLICATE TIME CHECK
        AppointmentCreateDTO duplicateDto = new AppointmentCreateDTO(doctor.getId(), patient.getId(), appointmentTime2, "Duplicate appointment");
        assertThrows(DuplicateResourceException.class, () -> appointmentService.create(duplicateDto));

        // DELETE
        appointmentService.delete(created.getId());
        assertThrows(EntityNotFoundException.class, () -> appointmentService.getById(created.getId()));
    }
}
