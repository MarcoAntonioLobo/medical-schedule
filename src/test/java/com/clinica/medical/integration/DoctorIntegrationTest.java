package com.clinica.medical.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.clinica.medical.model.Doctor;
import com.clinica.medical.repository.AppointmentRepository;
import com.clinica.medical.repository.DoctorRepository;
import com.clinica.medical.repository.PatientRepository;

@SpringBootTest
@ActiveProfiles("test")
public class DoctorIntegrationTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        // Apaga primeiro as entidades dependentes
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
    }

    @Test
    void fullDoctorLifecycle() {
        // CREATE
        Doctor doctor = Doctor.builder()
                .name("Dr. Integration")
                .specialty("Neurology")
                .crm("CRM987")
                .build();
        Doctor saved = doctorRepository.save(doctor);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Dr. Integration");

        // GET BY ID
        Doctor found = doctorRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        assertThat(found.getName()).isEqualTo("Dr. Integration");

        // GET ALL
        List<Doctor> all = doctorRepository.findAll();
        assertThat(all).hasSize(1);

        // UPDATE
        found.setSpecialty("Cardiology");
        Doctor updated = doctorRepository.save(found);
        assertThat(updated.getSpecialty()).isEqualTo("Cardiology");

        // DELETE
        doctorRepository.delete(updated);

        assertThrows(RuntimeException.class, () ->
                doctorRepository.findById(updated.getId())
                        .orElseThrow(() -> new RuntimeException("Doctor not found")));
    }

    @Test
    void deleteNonExistentDoctor_throwsException() {
        assertThrows(RuntimeException.class, () ->
                doctorRepository.findById(999L)
                        .orElseThrow(() -> new RuntimeException("Doctor not found")));
    }
}
