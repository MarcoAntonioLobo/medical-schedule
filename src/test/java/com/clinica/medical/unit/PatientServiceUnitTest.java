package com.clinica.medical.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.clinica.medical.dto.PatientCreateDTO;
import com.clinica.medical.dto.PatientDTO;
import com.clinica.medical.exceptions.DuplicateResourceException;
import com.clinica.medical.exceptions.EntityNotFoundException;
import com.clinica.medical.model.Patient;
import com.clinica.medical.repository.PatientRepository;
import com.clinica.medical.service.impl.PatientServiceImpl;

public class PatientServiceUnitTest {

    private PatientRepository patientRepository;
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientCreateDTO patientCreateDTO;

    @BeforeEach
    void setUp() {
        patientRepository = Mockito.mock(PatientRepository.class);
        patientService = new PatientServiceImpl(patientRepository);

        patient = Patient.builder()
                .id(1L)
                .name("Test Patient")
                .cpf("111.222.333-44")
                .phone("99999-0000")
                .build();

        patientCreateDTO = PatientCreateDTO.builder()
                .name("Test Patient")
                .cpf("111.222.333-44")
                .phone("99999-0000")
                .build();
    }

    @Test
    void createPatient_success() {
        when(patientRepository.existsByCpf(patientCreateDTO.getCpf())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientDTO created = patientService.create(patientCreateDTO);

        assertThat(created.getName()).isEqualTo(patientCreateDTO.getName());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void createPatient_duplicateCpf() {
        when(patientRepository.existsByCpf(patientCreateDTO.getCpf())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> patientService.create(patientCreateDTO));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void getPatientById_success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientDTO found = patientService.getById(1L);

        assertThat(found.getCpf()).isEqualTo(patient.getCpf());
    }

    @Test
    void getPatientById_notFound() {
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> patientService.getById(2L));
    }

    @Test
    void updatePatient_success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.existsByCpf(patientCreateDTO.getCpf())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientDTO updated = patientService.update(1L, patientCreateDTO);

        assertThat(updated.getName()).isEqualTo(patientCreateDTO.getName());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_duplicateCpf() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.existsByCpf("999.999.999-99")).thenReturn(true);

        PatientCreateDTO dto = PatientCreateDTO.builder()
                .name("New Name")
                .cpf("999.999.999-99")
                .phone("12345-6789")
                .build();

        assertThrows(DuplicateResourceException.class, () -> patientService.update(1L, dto));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void deletePatient_success() {
        when(patientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(patientRepository).deleteById(1L);

        patientService.delete(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatient_notFound() {
        when(patientRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> patientService.delete(2L));
        verify(patientRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllPatients_success() {
        List<Patient> patients = new ArrayList<>();
        patients.add(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        List<PatientDTO> allPatients = patientService.getAll();

        assertThat(allPatients).hasSize(1);
        assertThat(allPatients.get(0).getCpf()).isEqualTo(patient.getCpf());
    }
}
