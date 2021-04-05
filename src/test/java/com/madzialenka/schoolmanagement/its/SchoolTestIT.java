package com.madzialenka.schoolmanagement.its;

import com.madzialenka.schoolmanagement.api.dto.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class SchoolTestIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void testSchoolCreation() {
        RestAssured.with()
                .body(SchoolDataRequestDTO.builder()
                        .town("Warsaw")
                        .schoolNumber("II")
                        .subjects(Arrays.asList(
                                new SchoolSubjectDataRequestDTO("spanish", "Manny Delgado"),
                                new SchoolSubjectDataRequestDTO("astronomy", "Ida Star")
                        ))
                        .build())
                .header("Content-Type", "application/json")
                .when()
                .request("POST", "/schools")
                .then()
                .statusCode(200);

        Response response = RestAssured.with()
                .when()
                .request("GET", "/schools")
                .thenReturn();
        PageResponseDTO pageResponseDTO = response
                .as(PageResponseDTO.class);
        List<SchoolResponseDTO> elements = response
                .jsonPath()
                .getList("elements", SchoolResponseDTO.class);
        Assertions.assertEquals(1, pageResponseDTO.getNumberOfElements());
        SchoolResponseDTO school = elements.get(0);
        Assertions.assertEquals("Warsaw", school.getTown());
        Assertions.assertEquals("II", school.getSchoolNumber());
        List<SchoolSubjectResponseDTO> actualSubjects = school.getSubjects();
        actualSubjects.forEach(subject -> subject.setId(null));
        List<SchoolSubjectResponseDTO> expectedSubjects = Arrays.asList(
                new SchoolSubjectResponseDTO(null, "spanish", "Manny Delgado"),
                new SchoolSubjectResponseDTO(null, "astronomy", "Ida Star")
        );
        org.assertj.core.api.Assertions.assertThat(actualSubjects).hasSameElementsAs(expectedSubjects);
    }
}
