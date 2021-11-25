package tests;

import lombok.LombokUserData;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.Specification.request;
import static tests.Specification.responseSpec;

public class ReqressinTests {
    @Test
    void test01_getSingleUserWithLombok() {
        LombokUserData data = given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(LombokUserData.class);
        assertEquals(2, data.getUser().getId());
        assertEquals("janet.weaver@reqres.in", data.getUser().getEmail());
        assertEquals("Janet", data.getUser().getFirstName());
        assertEquals("Weaver", data.getUser().getLastName());
    }

    @Test
    void test02_checkEmailWithGroovy() {
        given()
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .log().body()
                .body("page", is(2))
                .body("data.findAll{it.id}.id.flatten()", hasItem(12))
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()", hasItem("byron.fields@reqres.in"))
                .body("data.findAll{it.first_name}.first_name.flatten()", hasItem("George"))
                .body("data.findAll{it.last_name}.last_name.flatten()", hasItem("Fields"));
        ;
    }
}

