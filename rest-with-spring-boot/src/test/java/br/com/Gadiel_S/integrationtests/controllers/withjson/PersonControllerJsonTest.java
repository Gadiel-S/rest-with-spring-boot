package br.com.Gadiel_S.integrationtests.controllers.withjson;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.dto.AccountCredentialsDTO;
import br.com.Gadiel_S.integrationtests.dto.PersonDTO;
import br.com.Gadiel_S.integrationtests.dto.TokenDTO;
import br.com.Gadiel_S.integrationtests.dto.wrappers.json.WrapperPersonDTO;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

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

    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
        .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
        .setBasePath("/api/person/v1")
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }

  @Test
  @Order(1)
  void createTest() throws JsonProcessingException {
    mockPerson();

    var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(person)
        .when()
          .post()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
    person = createdPerson;

    assertNotNull(createdPerson.getId());
    assertTrue(createdPerson.getId() > 0);

    assertEquals("Linus", createdPerson.getFirstName());
    assertEquals("Torvalds", createdPerson.getLastName());
    assertEquals("Helsinki - Finland", createdPerson.getAddress());
    assertEquals("Male", createdPerson.getGender());
    assertTrue(createdPerson.getEnabled());
  }

  @Test
  @Order(2)
  void updateTest() throws JsonProcessingException {
    person.setLastName("Benedict Torvalds");

    var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(person)
        .when()
          .put()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
    person = createdPerson;

    assertNotNull(createdPerson.getId());
    assertTrue(createdPerson.getId() > 0);

    assertEquals("Linus", createdPerson.getFirstName());
    assertEquals("Benedict Torvalds", createdPerson.getLastName());
    assertEquals("Helsinki - Finland", createdPerson.getAddress());
    assertEquals("Male", createdPerson.getGender());
    assertTrue(createdPerson.getEnabled());
  }

  @Test
  @Order(3)
  void findByIdTest() throws JsonProcessingException {
    var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
          .pathParam("id", person.getId())
        .when()
          .get("{id}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
    person = createdPerson;

    assertNotNull(createdPerson.getId());
    assertTrue(createdPerson.getId() > 0);

    assertEquals("Linus", createdPerson.getFirstName());
    assertEquals("Benedict Torvalds", createdPerson.getLastName());
    assertEquals("Helsinki - Finland", createdPerson.getAddress());
    assertEquals("Male", createdPerson.getGender());
    assertTrue(createdPerson.getEnabled());
  }

  @Test
  @Order(4)
  void disableTest() throws JsonProcessingException {
    var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
          .pathParam("id", person.getId())
        .when()
          .patch("{id}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
    person = createdPerson;

    assertNotNull(createdPerson.getId());
    assertTrue(createdPerson.getId() > 0);

    assertEquals("Linus", createdPerson.getFirstName());
    assertEquals("Benedict Torvalds", createdPerson.getLastName());
    assertEquals("Helsinki - Finland", createdPerson.getAddress());
    assertEquals("Male", createdPerson.getGender());
    assertFalse(createdPerson.getEnabled());
  }

  @Test
  @Order(5)
  void deleteTest() {
    given(specification)
          .pathParam("id", person.getId())
        .when()
          .delete("{id}")
        .then()
          .statusCode(204);
  }

  @Test
  @Order(6)
  void findAllTest() throws JsonProcessingException {
    var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .queryParams("page", 3, "size", 12, "direction", "asc")
        .when()
          .get()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
    List<PersonDTO> people = wrapper.getEmbedded().getPeople();

    PersonDTO personOne = people.getFirst();

    assertNotNull(personOne.getId());
    assertTrue(personOne.getId() > 0);

    assertEquals("Alwin", personOne.getFirstName());
    assertEquals("Flockhart", personOne.getLastName());
    assertEquals("81 Forest Run Alley", personOne.getAddress());
    assertEquals("Male", personOne.getGender());
    assertFalse(personOne.getEnabled());

    PersonDTO personThree = people.get(2);

    assertNotNull(personThree.getId());
    assertTrue(personThree.getId() > 0);

    assertEquals("Rockey", personThree.getFirstName());
    assertEquals("Fritchley", personThree.getLastName());
    assertEquals("2 Tennyson Drive", personThree.getAddress());
    assertEquals("Male", personThree.getGender());
    assertFalse(personThree.getEnabled());
  }

  @Test
  @Order(7)
  void findByNameTest() throws JsonProcessingException {
    // {{baseUrl}}/api/person/v1/findPeopleByName/and?page=0&size=12&direction=asc
    var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("firstName", "and")
        .queryParams("page", 0, "size", 12, "direction", "asc")
        .when()
          .get("findPeopleByName/{firstName}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
    List<PersonDTO> people = wrapper.getEmbedded().getPeople();

    PersonDTO personOne = people.getFirst();

    assertNotNull(personOne.getId());
    assertTrue(personOne.getId() > 0);

    assertEquals("Alessandro", personOne.getFirstName());
    assertEquals("McFaul", personOne.getLastName());
    assertEquals("5 Lukken Plaza", personOne.getAddress());
    assertEquals("Male", personOne.getGender());
    assertTrue(personOne.getEnabled());

    PersonDTO personThree = people.get(2);

    assertNotNull(personThree.getId());
    assertTrue(personThree.getId() > 0);

    assertEquals("Bertrando", personThree.getFirstName());
    assertEquals("Becconsall", personThree.getLastName());
    assertEquals("35 Dryden Junction", personThree.getAddress());
    assertEquals("Male", personThree.getGender());
    assertTrue(personThree.getEnabled());
  }

  private void mockPerson() {
    person.setFirstName("Linus");
    person.setLastName("Torvalds");
    person.setAddress("Helsinki - Finland");
    person.setGender("Male");
    person.setEnabled(true);
    person.setProfileUrl("https://github.com/Gadiel-S");
    person.setPhotoUrl("https://raw.githubusercontent.com/leandrocgsi/rest-with-spring-boot-and-java-erudio/refs/heads/main/photos/00_some_person.jpg");
  }
}