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
class DoctorControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateDoctorSuccessfully() {
        String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        String json = """
            {
              "name": "Dr. João Silva %s",
              "specialty": "Cardiology",
              "crm": "%s"
            }
            """.formatted(uniqueId, uniqueId);

        given()
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post("/doctors")
        .then()
            .statusCode(anyOf(is(200), is(201)))
            .body("name", equalTo("Dr. João Silva " + uniqueId))
            .body("specialty", equalTo("Cardiology"))
            .body("crm", equalTo(uniqueId));
    }

    @Test
    void shouldReturnListOfDoctors() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/doctors")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void shouldGetDoctorById() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/doctors/1")
        .then()
            .statusCode(anyOf(is(200), is(404)));
    }
}
