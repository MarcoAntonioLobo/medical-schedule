package com.clinica.medical.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.medical.dto.PatientCreateDTO;
import com.clinica.medical.dto.PatientDTO;
import com.clinica.medical.model.Patient;
import com.clinica.medical.repository.PatientRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "Endpoints for managing patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PostMapping
    @Operation(summary = "Create a new patient", 
               description = "Creates a new patient in the system", 
               tags = { "Patients" },
               responses = {
                   @ApiResponse(responseCode = "201", description = "Patient created successfully"),
                   @ApiResponse(responseCode = "400", description = "Invalid input")
               })
    public ResponseEntity<PatientDTO> create(@Valid @RequestBody PatientCreateDTO dto) {
        Patient saved = patientRepository.save(new Patient(null, dto.getName(), dto.getCpf(), dto.getPhone()));
        return ResponseEntity
                .status(HttpStatus.CREATED)  // retorna 201
                .body(new PatientDTO(saved.getId(), saved.getName(), saved.getCpf(), saved.getPhone()));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieves a patient by their ID", tags = { "Patients" })
    public ResponseEntity<PatientDTO> getById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(p -> ResponseEntity.ok(new PatientDTO(p.getId(), p.getName(), p.getCpf(), p.getPhone())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieves all patients in the system", tags = { "Patients" })
    public ResponseEntity<List<PatientDTO>> getAll() {
        List<PatientDTO> list = patientRepository.findAll().stream()
                .map(p -> new PatientDTO(p.getId(), p.getName(), p.getCpf(), p.getPhone()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete patient by ID",
        description = "Deletes a patient from the system by their ID",
        tags = { "Patients" },
        responses = {
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
        }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!patientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        patientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
