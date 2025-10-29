package com.clinica.medical.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PatientControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreatePatientSuccessfully() {
        String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 11);
        String json = """
            {
              "name": "Maria Oliveira %s",
              "cpf": "%s",
              "phone": "11987654321"
            }
            """.formatted(uniqueId, uniqueId);

        given()
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post("/patients")
        .then()
            .statusCode(anyOf(is(200), is(201)))
            .body("name", equalTo("Maria Oliveira " + uniqueId))
            .body("cpf", equalTo(uniqueId))
            .body("phone", equalTo("11987654321"));
    }

    @Test
    void shouldReturnListOfPatients() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/patients")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void shouldGetPatientById() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/patients/1")
        .then()
            .statusCode(anyOf(is(200), is(404)));
    }
}
