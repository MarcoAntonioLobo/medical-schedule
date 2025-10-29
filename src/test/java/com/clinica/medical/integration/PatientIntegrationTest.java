package com.clinica.medical.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.clinica.medical.model.Patient;
import com.clinica.medical.repository.AppointmentRepository;
import com.clinica.medical.repository.DoctorRepository;
import com.clinica.medical.repository.PatientRepository;

@SpringBootTest
@ActiveProfiles("test")
public class PatientIntegrationTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        // Limpa na ordem certa para evitar violação de chave estrangeira
        appointmentRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
    }

    @Test
    void fullPatientLifecycle() {
        // CREATE
        Patient patient = Patient.builder()
                .name("Test Patient")
                .cpf("111.222.333-44")
                .phone("99999-8888")
                .build();
        Patient saved = patientRepository.save(patient);
        assertThat(saved.getId()).isNotNull();

        // GET BY ID
        Patient found = patientRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        assertThat(found.getName()).isEqualTo("Test Patient");

        // GET ALL
        List<Patient> all = patientRepository.findAll();
        assertThat(all).hasSize(1);

        // UPDATE
        found.setPhone("88888-7777");
        Patient updated = patientRepository.save(found);
        assertThat(updated.getPhone()).isEqualTo("88888-7777");

        // DELETE
        patientRepository.delete(updated);
        assertThrows(RuntimeException.class, () ->
                patientRepository.findById(updated.getId())
                        .orElseThrow(() -> new RuntimeException("Patient not found")));
    }

    @Test
    void deleteNonExistentPatient_throwsException() {
        assertThrows(RuntimeException.class, () ->
                patientRepository.findById(999L)
                        .orElseThrow(() -> new RuntimeException("Patient not found")));
    }
}
