package br.com.Gadiel_S.integrationtests.controllers.cors.withjson;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.dto.AccountCredentialsDTO;
import br.com.Gadiel_S.integrationtests.dto.PersonDTO;
import br.com.Gadiel_S.integrationtests.dto.TokenDTO;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerCorsTest extends AbstractIntegrationTest {

  @LocalServerPort
  private int port;

  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;
  private static PersonDTO person;
  private static TokenDTO token;

  @BeforeAll
  static void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    person = new PersonDTO();
    token = new TokenDTO();
  }

  @BeforeEach
  void setUpEach() {
    RestAssured.port = port;
  }

  @Test
  @Order(0)
  void signIn() {
    AccountCredentialsDTO credentials = new AccountCredentialsDTO("gadiel", "admin123");

    token = given()
        .basePath("/auth/signin")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(credentials)
        .when()
        .post()
        .then()
        .statusCode(200)
        .extract()
        .body()
        .as(TokenDTO.class);

    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }

  @Test
  @Order(1)
  void create() throws JsonProcessingException {
    mockPerson();
    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
        .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
        .setBasePath("/api/person/v1")
          .addFilter(new RequestLoggingFilter(LogDetail.ALL))
          .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(person)
        .when()
          .post()
        .then()
          .statusCode(200)
        .extract()
          .body()
            .asString();

    PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
    person = createdPerson;

    assertNotNull(createdPerson.getId());
    assertNotNull(createdPerson.getFirstName());
    assertNotNull(createdPerson.getLastName());
    assertNotNull(createdPerson.getAddress());
    assertNotNull(createdPerson.getGender());
    assertTrue(createdPerson.getId() > 0);

    assertEquals("Richard", createdPerson.getFirstName());
    assertEquals("Stallman", createdPerson.getLastName());
    assertEquals("New York City - New York - USA", createdPerson.getAddress());
    assertEquals("Male", createdPerson.getGender());
    assertTrue(createdPerson.getEnabled());
  }

  @Test
  @Order(2)
  void createWithWrongOrigin() {
    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LINKEDIN)
        .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
        .setBasePath("/api/person/v1")
          .addFilter(new RequestLoggingFilter(LogDetail.ALL))
          .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(person)
        .when()
          .post()
        .then()
          .statusCode(403)
        .extract()
          .body()
            .asString();

    assertEquals("Invalid CORS request", content);
  }

  @Test
  @Order(3)
  void findById() throws JsonProcessingException {
    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
        .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
        .setBasePath("/api/person/v1")
          .addFilter(new RequestLoggingFilter(LogDetail.ALL))
          .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
          .pathParam("id", person.getId())
        .when()
          .get("{id}")
        .then()
         .statusCode(200)
        .extract()
         .body()
          .asString();

    PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
    person = createdPerson;

    assertNotNull(createdPerson.getId());
    assertNotNull(createdPerson.getFirstName());
    assertNotNull(createdPerson.getLastName());
    assertNotNull(createdPerson.getAddress());
    assertNotNull(createdPerson.getGender());
    assertTrue(createdPerson.getId() > 0);

    assertEquals("Richard", createdPerson.getFirstName());
    assertEquals("Stallman", createdPerson.getLastName());
    assertEquals("New York City - New York - USA", createdPerson.getAddress());
    assertEquals("Male", createdPerson.getGender());
    assertTrue(createdPerson.getEnabled());
  }

  @Test
  @Order(4)
  void findByIdWithWrongOrigin() {
    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LINKEDIN)
        .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
        .setBasePath("/api/person/v1")
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", person.getId())
        .when()
        .get("{id}")
        .then()
        .statusCode(403)
        .extract()
        .body()
        .asString();

    assertEquals("Invalid CORS request", content);
  }

  private void mockPerson() {
    person.setFirstName("Richard");
    person.setLastName("Stallman");
    person.setAddress("New York City - New York - USA");
    person.setGender("Male");
    person.setEnabled(true);
  }
}