package com.clinica.medical.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.clinica.medical.model.Appointment;
import com.clinica.medical.model.Doctor;
import com.clinica.medical.model.Patient;
import com.clinica.medical.repository.AppointmentRepository;
import com.clinica.medical.repository.DoctorRepository;
import com.clinica.medical.repository.PatientRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AppointmentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Long patientId;
    private Long doctorId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();

        Patient patient = patientRepository.save(
                new Patient(null, "Maria Oliveira", "123456" + System.currentTimeMillis(), "11987654321"));
        patientId = patient.getId();

        Doctor doctor = doctorRepository.save(
                new Doctor(null, "Dr. João Silva", "Cardiology", "CRM" + System.currentTimeMillis()));
        doctorId = doctor.getId();
    }

    @Test
    void shouldCreateAppointmentSuccessfully() {
        String json = String.format("""
            {
              "doctorId": %d,
              "patientId": %d,
              "dateTime": "%s",
              "notes": "Consulta de rotina"
            }
            """, doctorId, patientId, LocalDateTime.now().plusDays(1));

        given()
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post("/appointments")
        .then()
            .statusCode(201)
            .body("doctorName", equalTo("Dr. João Silva"))
            .body("patientName", equalTo("Maria Oliveira"))
            .body("dateTime", notNullValue())
            .body("notes", equalTo("Consulta de rotina"));
    }

    @Test
    void shouldReturnListOfAppointments() {
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctorRepository.findById(doctorId).get());
        appointment.setPatient(patientRepository.findById(patientId).get());
        appointment.setDateTime(LocalDateTime.now().plusDays(1));
        appointment.setNotes("Consulta de rotina");
        appointmentRepository.save(appointment);

        given()
            .accept(ContentType.JSON)
        .when()
            .get("/appointments")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }
}
