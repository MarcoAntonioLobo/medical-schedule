package com.clinica.medical.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clinica.medical.dto.PatientCreateDTO;
import com.clinica.medical.dto.PatientDTO;
import com.clinica.medical.exceptions.DuplicateResourceException;
import com.clinica.medical.exceptions.EntityNotFoundException;
import com.clinica.medical.model.Patient;
import com.clinica.medical.repository.PatientRepository;
import com.clinica.medical.service.PatientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public PatientDTO create(PatientCreateDTO dto) {
        if (patientRepository.existsByCpf(dto.getCpf())) {
            throw new DuplicateResourceException("CPF already exists");
        }
        Patient patient = Patient.builder()
                .name(dto.getName())
                .cpf(dto.getCpf())
                .phone(dto.getPhone())
                .build();

        Patient saved = patientRepository.save(patient);
        return mapToDTO(saved);
    }

    @Override
    public PatientDTO getById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        return mapToDTO(patient);
    }

    @Override
    public List<PatientDTO> getAll() {
        return patientRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO update(Long id, PatientCreateDTO dto) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        if (!existing.getCpf().equals(dto.getCpf()) && patientRepository.existsByCpf(dto.getCpf())) {
            throw new DuplicateResourceException("CPF already exists");
        }

        existing.setName(dto.getName());
        existing.setCpf(dto.getCpf());
        existing.setPhone(dto.getPhone());

        Patient saved = patientRepository.save(existing);
        return mapToDTO(saved);
    }

    @Override
    public void delete(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    private PatientDTO mapToDTO(Patient patient) {
        return PatientDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .cpf(patient.getCpf())
                .phone(patient.getPhone())
                .build();
    }
}
