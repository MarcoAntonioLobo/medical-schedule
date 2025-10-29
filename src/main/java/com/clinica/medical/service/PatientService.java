package com.clinica.medical.service;

import java.util.List;

import com.clinica.medical.dto.PatientCreateDTO;
import com.clinica.medical.dto.PatientDTO;

public interface PatientService {

    PatientDTO create(PatientCreateDTO dto);

    PatientDTO getById(Long id);

    List<PatientDTO> getAll();

    PatientDTO update(Long id, PatientCreateDTO dto);

    void delete(Long id);
}
