package com.clinica.medical.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clinica.medical.dto.DoctorCreateDTO;
import com.clinica.medical.dto.DoctorDTO;
import com.clinica.medical.exceptions.DuplicateResourceException;
import com.clinica.medical.exceptions.EntityNotFoundException;
import com.clinica.medical.model.Doctor;
import com.clinica.medical.repository.DoctorRepository;
import com.clinica.medical.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorDTO create(DoctorCreateDTO dto) {
        if (doctorRepository.existsByCrm(dto.getCrm())) {
            throw new DuplicateResourceException("CRM already exists");
        }
        Doctor saved = doctorRepository.save(new Doctor(null, dto.getName(), dto.getSpecialty(), dto.getCrm()));
        return new DoctorDTO(saved.getId(), saved.getName(), saved.getSpecialty(), saved.getCrm());
    }

    @Override
    public DoctorDTO getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        return new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSpecialty(), doctor.getCrm());
    }

    @Override
    public List<DoctorDTO> getAll() {
        return doctorRepository.findAll().stream()
                .map(d -> new DoctorDTO(d.getId(), d.getName(), d.getSpecialty(), d.getCrm()))
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO update(Long id, DoctorCreateDTO dto) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        if (!existing.getCrm().equals(dto.getCrm()) && doctorRepository.existsByCrm(dto.getCrm())) {
            throw new DuplicateResourceException("CRM already exists");
        }

        existing.setName(dto.getName());
        existing.setSpecialty(dto.getSpecialty());
        existing.setCrm(dto.getCrm());
        Doctor saved = doctorRepository.save(existing);

        return new DoctorDTO(saved.getId(), saved.getName(), saved.getSpecialty(), saved.getCrm());
    }

    @Override
    public void delete(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor not found");
        }
        doctorRepository.deleteById(id);
    }
}
