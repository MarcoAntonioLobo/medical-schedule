package com.clinica.medical.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.medical.dto.AppointmentCreateDTO;
import com.clinica.medical.dto.AppointmentResponseDTO;
import com.clinica.medical.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Appointments", description = "Endpoints for managing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @Operation(summary = "Create a new appointment", description = "Creates a new appointment in the system", tags = { "Appointments" })
    public ResponseEntity<AppointmentResponseDTO> create(@Valid @RequestBody AppointmentCreateDTO dto) {
        return ResponseEntity.ok(appointmentService.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieves an appointment by its ID", tags = { "Appointments" })
    public ResponseEntity<AppointmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all appointments", description = "Retrieves all appointments in the system", tags = { "Appointments" })
    public ResponseEntity<List<AppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing appointment", description = "Updates an existing appointment by ID", tags = { "Appointments" })
    public ResponseEntity<AppointmentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AppointmentCreateDTO dto) {
        return ResponseEntity.ok(appointmentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an appointment by ID", description = "Deletes an appointment from the system by its ID", tags = { "Appointments" })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
