package com.clinica.medical.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.clinica.medical.dto.AppointmentCreateDTO;
import com.clinica.medical.dto.AppointmentResponseDTO;
import com.clinica.medical.exceptions.DuplicateResourceException;
import com.clinica.medical.exceptions.EntityNotFoundException;
import com.clinica.medical.mapper.AppointmentMapper;
import com.clinica.medical.model.Appointment;
import com.clinica.medical.model.Doctor;
import com.clinica.medical.model.Patient;
import com.clinica.medical.repository.AppointmentRepository;
import com.clinica.medical.repository.DoctorRepository;
import com.clinica.medical.repository.PatientRepository;
import com.clinica.medical.service.impl.AppointmentServiceImpl;

public class AppointmentServiceUnitTest {

    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private AppointmentMapper mapper;
    private AppointmentServiceImpl appointmentService;

    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentRepository = Mockito.mock(AppointmentRepository.class);
        doctorRepository = Mockito.mock(DoctorRepository.class);
        patientRepository = Mockito.mock(PatientRepository.class);
        mapper = Mockito.mock(AppointmentMapper.class);

        appointmentService = new AppointmentServiceImpl(appointmentRepository, doctorRepository, patientRepository, mapper);

        doctor = Doctor.builder().id(1L).name("Dr. Test").crm("CRM123").specialty("Cardiology").build();
        patient = Patient.builder().id(1L).name("Patient Test").cpf("111.222.333-44").phone("99999-0000").build();
        appointment = Appointment.builder().id(1L).doctor(doctor).patient(patient).dateTime(LocalDateTime.now()).notes("Notes").build();
    }

    @Test
    void createAppointment_success() {
        AppointmentCreateDTO dto = new AppointmentCreateDTO(doctor.getId(), patient.getId(), LocalDateTime.now(), "Notes");

        when(doctorRepository.findById(dto.getDoctorId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(dto.getPatientId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.existsByDoctorIdAndDateTime(dto.getDoctorId(), dto.getDateTime())).thenReturn(false);
        when(mapper.toEntity(dto, doctor, patient)).thenReturn(appointment);
        when(mapper.toResponse(appointment)).thenReturn(new AppointmentResponseDTO(1L, doctor.getName(), patient.getName(), appointment.getDateTime(), "Notes"));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        AppointmentResponseDTO response = appointmentService.create(dto);
        assertThat(response.getDoctorName()).isEqualTo("Dr. Test");
    }

    @Test
    void createAppointment_doctorNotFound() {
        AppointmentCreateDTO dto = new AppointmentCreateDTO(1L, 1L, LocalDateTime.now(), "Notes");
        when(doctorRepository.findById(dto.getDoctorId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> appointmentService.create(dto));
    }

    @Test
    void createAppointment_patientNotFound() {
        AppointmentCreateDTO dto = new AppointmentCreateDTO(1L, 1L, LocalDateTime.now(), "Notes");
        when(doctorRepository.findById(dto.getDoctorId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(dto.getPatientId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> appointmentService.create(dto));
    }

    @Test
    void createAppointment_duplicate() {
        AppointmentCreateDTO dto = new AppointmentCreateDTO(1L, 1L, LocalDateTime.now(), "Notes");
        when(doctorRepository.findById(dto.getDoctorId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(dto.getPatientId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.existsByDoctorIdAndDateTime(dto.getDoctorId(), dto.getDateTime())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> appointmentService.create(dto));
    }

    @Test
    void getById_success() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(mapper.toResponse(appointment)).thenReturn(new AppointmentResponseDTO(1L, doctor.getName(), patient.getName(), appointment.getDateTime(), "Notes"));

        AppointmentResponseDTO response = appointmentService.getById(1L);
        assertThat(response.getPatientName()).isEqualTo("Patient Test");
    }

    @Test
    void getById_notFound() {
        when(appointmentRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> appointmentService.getById(2L));
    }

    @Test
    void getAll_success() {
        Appointment anotherAppointment = Appointment.builder().id(2L).doctor(doctor).patient(patient).dateTime(LocalDateTime.now()).notes("Another").build();
        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(appointment, anotherAppointment));
        when(mapper.toResponse(appointment)).thenReturn(new AppointmentResponseDTO(1L, doctor.getName(), patient.getName(), appointment.getDateTime(), "Notes"));
        when(mapper.toResponse(anotherAppointment)).thenReturn(new AppointmentResponseDTO(2L, doctor.getName(), patient.getName(), anotherAppointment.getDateTime(), "Another"));

        List<AppointmentResponseDTO> list = appointmentService.getAll();
        assertThat(list).hasSize(2);
    }

    @Test
    void updateAppointment_success() {
        AppointmentCreateDTO dto = new AppointmentCreateDTO(doctor.getId(), patient.getId(), LocalDateTime.now(), "Updated Notes");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(dto.getDoctorId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(dto.getPatientId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.existsByDoctorIdAndDateTime(dto.getDoctorId(), dto.getDateTime())).thenReturn(false);
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(mapper.toResponse(appointment)).thenReturn(new AppointmentResponseDTO(1L, doctor.getName(), patient.getName(), dto.getDateTime(), "Updated Notes"));

        AppointmentResponseDTO response = appointmentService.update(1L, dto);
        assertThat(response.getNotes()).isEqualTo("Updated Notes");
    }

    @Test
    void updateAppointment_duplicate() {
        AppointmentCreateDTO dto = new AppointmentCreateDTO(doctor.getId(), patient.getId(), LocalDateTime.now(), "Updated Notes");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(dto.getDoctorId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(dto.getPatientId())).thenReturn(Optional.of(patient));        // simula duplicidade corretamente
        when(appointmentRepository.existsByDoctorIdAndDateTimeAndIdNot(dto.getDoctorId(), dto.getDateTime(), 1L))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> appointmentService.update(1L, dto));
    }

    @Test
    void updateAppointment_notFound() {
        AppointmentCreateDTO dto = new AppointmentCreateDTO(doctor.getId(), patient.getId(), LocalDateTime.now(), "Updated Notes");
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> appointmentService.update(1L, dto));
    }

    @Test
    void deleteAppointment_success() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(appointmentRepository).deleteById(1L);

        appointmentService.delete(1L);
        verify(appointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAppointment_notFound() {
        when(appointmentRepository.existsById(2L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> appointmentService.delete(2L));
    }
}
