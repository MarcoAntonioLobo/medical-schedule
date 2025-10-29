package com.clinica.medical.service;

import java.util.List;

import com.clinica.medical.dto.AppointmentCreateDTO;
import com.clinica.medical.dto.AppointmentResponseDTO;

public interface AppointmentService {

    AppointmentResponseDTO create(AppointmentCreateDTO dto);

    AppointmentResponseDTO getById(Long id);

    List<AppointmentResponseDTO> getAll();

    AppointmentResponseDTO update(Long id, AppointmentCreateDTO dto);

    void delete(Long id);
}
