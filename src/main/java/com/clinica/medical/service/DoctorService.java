package com.clinica.medical.service;

import java.util.List;

import com.clinica.medical.dto.DoctorCreateDTO;
import com.clinica.medical.dto.DoctorDTO;

public interface DoctorService {

    DoctorDTO create(DoctorCreateDTO dto);

    DoctorDTO getById(Long id);

    List<DoctorDTO> getAll();

    DoctorDTO update(Long id, DoctorCreateDTO dto);

    void delete(Long id);
}
