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

import com.clinica.medical.dto.DoctorCreateDTO;
import com.clinica.medical.dto.DoctorDTO;
import com.clinica.medical.exceptions.DuplicateResourceException;
import com.clinica.medical.exceptions.EntityNotFoundException;
import com.clinica.medical.model.Doctor;
import com.clinica.medical.repository.DoctorRepository;
import com.clinica.medical.service.impl.DoctorServiceImpl;

public class DoctorServiceUnitTest {

    private DoctorRepository doctorRepository;
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorCreateDTO doctorCreateDTO;

    @BeforeEach
    void setUp() {
        doctorRepository = Mockito.mock(DoctorRepository.class);
        doctorService = new DoctorServiceImpl(doctorRepository);

        doctor = Doctor.builder()
                .id(1L)
                .name("Dr. Test")
                .crm("CRM123")
                .specialty("Cardiology")
                .build();

        doctorCreateDTO = DoctorCreateDTO.builder()
                .name("Dr. Test")
                .crm("CRM123")
                .specialty("Cardiology")
                .build();
    }

    @Test
    void createDoctor_success() {
        when(doctorRepository.existsByCrm(doctorCreateDTO.getCrm())).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorDTO created = doctorService.create(doctorCreateDTO);

        assertThat(created.getName()).isEqualTo(doctorCreateDTO.getName());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void createDoctor_duplicateCrm() {
        when(doctorRepository.existsByCrm(doctorCreateDTO.getCrm())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> doctorService.create(doctorCreateDTO));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void getDoctorById_success() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorDTO found = doctorService.getById(1L);

        assertThat(found.getCrm()).isEqualTo(doctor.getCrm());
    }

    @Test
    void getDoctorById_notFound() {
        when(doctorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> doctorService.getById(2L));
    }

    @Test
    void updateDoctor_success() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByCrm(doctorCreateDTO.getCrm())).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorDTO updated = doctorService.update(1L, doctorCreateDTO);

        assertThat(updated.getSpecialty()).isEqualTo(doctorCreateDTO.getSpecialty());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void updateDoctor_duplicateCrm() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByCrm("CRM999")).thenReturn(true);

        DoctorCreateDTO dto = DoctorCreateDTO.builder()
                .name("Dr. New")
                .crm("CRM999")
                .specialty("Neurology")
                .build();

        assertThrows(DuplicateResourceException.class, () -> doctorService.update(1L, dto));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void deleteDoctor_success() {
        when(doctorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(doctorRepository).deleteById(1L);

        doctorService.delete(1L);
        verify(doctorRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDoctor_notFound() {
        when(doctorRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> doctorService.delete(2L));
        verify(doctorRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllDoctors_success() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<DoctorDTO> allDoctors = doctorService.getAll();

        assertThat(allDoctors).hasSize(1);
        assertThat(allDoctors.get(0).getCrm()).isEqualTo(doctor.getCrm());
    }
}
