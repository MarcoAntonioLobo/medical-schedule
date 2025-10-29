package com.clinica.medical.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica.medical.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    boolean existsByDoctorIdAndDateTime(Long doctorId, LocalDateTime dateTime);

    boolean existsByDoctorIdAndDateTimeAndIdNot(Long doctorId, LocalDateTime dateTime, Long id);
}