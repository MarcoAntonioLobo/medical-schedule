package com.clinica.medical.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
import com.clinica.medical.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper mapper;

    @Override
    public AppointmentResponseDTO create(AppointmentCreateDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        if (appointmentRepository.existsByDoctorIdAndDateTime(doctor.getId(), dto.getDateTime())) {
            throw new DuplicateResourceException("Doctor already has an appointment at this time");
        }

        Appointment appointment = mapper.toEntity(dto, doctor, patient);
        Appointment saved = appointmentRepository.save(appointment);
        return mapper.toResponse(saved);
    }

    @Override
    public AppointmentResponseDTO getById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        return mapper.toResponse(appointment);
    }

    @Override
    public List<AppointmentResponseDTO> getAll() {
        return appointmentRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDTO update(Long id, AppointmentCreateDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        if (appointmentRepository.existsByDoctorIdAndDateTimeAndIdNot(doctor.getId(), dto.getDateTime(), id)) {
            throw new DuplicateResourceException("Doctor already has an appointment at this time");
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(dto.getDateTime());
        appointment.setNotes(dto.getNotes());

        Appointment saved = appointmentRepository.save(appointment);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Appointment not found");
        }
        appointmentRepository.deleteById(id);
    }
}
