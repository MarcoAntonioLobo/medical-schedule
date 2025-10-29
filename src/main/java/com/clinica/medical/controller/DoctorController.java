package com.clinica.medical.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.medical.dto.DoctorCreateDTO;
import com.clinica.medical.dto.DoctorDTO;
import com.clinica.medical.model.Doctor;
import com.clinica.medical.repository.DoctorRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Doctors", description = "Endpoints for managing doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostMapping
    @Operation(summary = "Create a new doctor", description = "Creates a new doctor in the system", tags = { "Doctors" })
    public ResponseEntity<DoctorDTO> create(@Valid @RequestBody DoctorCreateDTO dto) {
        Doctor saved = doctorRepository.save(new Doctor(null, dto.getName(), dto.getSpecialty(), dto.getCrm()));
        return ResponseEntity.ok(new DoctorDTO(saved.getId(), saved.getName(), saved.getSpecialty(), saved.getCrm()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieves a doctor by their ID", tags = { "Doctors" })
    public ResponseEntity<DoctorDTO> getById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(d -> ResponseEntity.ok(new DoctorDTO(d.getId(), d.getName(), d.getSpecialty(), d.getCrm())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieves all doctors in the system", tags = { "Doctors" })
    public ResponseEntity<List<DoctorDTO>> getAll() {
        List<DoctorDTO> list = doctorRepository.findAll().stream()
                .map(d -> new DoctorDTO(d.getId(), d.getName(), d.getSpecialty(), d.getCrm()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor by ID", description = "Deletes a doctor from the system by their ID", tags = { "Doctors" })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!doctorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        doctorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
