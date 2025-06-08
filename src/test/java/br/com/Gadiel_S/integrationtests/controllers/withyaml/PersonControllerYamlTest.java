package br.com.Gadiel_S.integrationtests.controllers.withyaml;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.data.dto.PersonDTO;
import br.com.Gadiel_S.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static YAMLMapper objectMapper;
  private static PersonDTO person;

  @BeforeAll
  static void setUp() {
    objectMapper = new YAMLMapper();
    person = new PersonDTO();
  }

  @Test
  @Order(1)
  void createTest() {
    mockPerson();
    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
        .setBasePath("/api/person/v1")
        .setPort(TestConfigs.SERVER_PORT)
          .addFilter(new RequestLoggingFilter(LogDetail.ALL))
          .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
          .setConfig(RestAssured.config()
              .encoderConfig(EncoderConfig.encoderConfig()
                  .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
        .build();

    var createdPerson = given(specification)
        .contentType(MediaType.APPLICATION_YAML_VALUE)
        .accept(MediaType.APPLICATION_YAML_VALUE)
          .body(person, objectMapper)
        .when()
          .post()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(PersonDTO.class, objectMapper);

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

    var createdPerson = given(specification)
        .contentType(MediaType.APPLICATION_YAML_VALUE)
        .accept(MediaType.APPLICATION_YAML_VALUE)
          .body(person, objectMapper)
        .when()
          .put()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(PersonDTO.class, objectMapper);

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
    var createdPerson = given(specification)
        .accept(MediaType.APPLICATION_YAML_VALUE)
          .pathParam("id", person.getId())
        .when()
          .get("{id}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(PersonDTO.class, objectMapper);

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
    var createdPerson = given(specification)
        .accept(MediaType.APPLICATION_YAML_VALUE)
          .pathParam("id", person.getId())
        .when()
          .patch("{id}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(PersonDTO.class, objectMapper);

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
    var response = given(specification)
        .accept(MediaType.APPLICATION_YAML_VALUE)
        .when()
          .get()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(PersonDTO[].class, objectMapper);

    List<PersonDTO> people = Arrays.asList(response);

    PersonDTO personOne = people.getFirst();

    assertNotNull(personOne.getId());
    assertTrue(personOne.getId() > 0);

    assertEquals("Ayrton", personOne.getFirstName());
    assertEquals("Senna", personOne.getLastName());
    assertEquals("São Paulo - Brasil - América", personOne.getAddress());
    assertEquals("Male", personOne.getGender());
    assertTrue(personOne.getEnabled());

    PersonDTO personThree = people.get(2);

    assertNotNull(personThree.getId());
    assertTrue(personThree.getId() > 0);

    assertEquals("Nelipe", personThree.getFirstName());
    assertEquals("Feto", personThree.getLastName());
    assertEquals("Rio de Janeiro - Brasil", personThree.getAddress());
    assertEquals("Male", personThree.getGender());
    assertTrue(personThree.getEnabled());
  }

  private void mockPerson() {
    person.setFirstName("Linus");
    person.setLastName("Torvalds");
    person.setAddress("Helsinki - Finland");
    person.setGender("Male");
    person.setEnabled(true);
  }
}