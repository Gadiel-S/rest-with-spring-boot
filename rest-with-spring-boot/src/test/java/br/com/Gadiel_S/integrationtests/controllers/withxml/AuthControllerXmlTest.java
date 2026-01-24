package br.com.Gadiel_S.integrationtests.controllers.withxml;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.dto.AccountCredentialsDTO;
import br.com.Gadiel_S.integrationtests.dto.TokenDTO;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {

  @LocalServerPort
  private int port;

  private static TokenDTO token;
  private static XmlMapper objectMapper;

  @BeforeAll
  static void setUp() {
    objectMapper = new XmlMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    token = new TokenDTO();
  }

  @BeforeEach
  void setUpEach() {
    RestAssured.port = port;
  }

  @Test
  @Order(1)
  void signIn() throws JsonProcessingException {
    AccountCredentialsDTO credentials = new AccountCredentialsDTO("gadiel", "admin123");

    var content = given()
        .basePath("/auth/signin")
        .contentType(MediaType.APPLICATION_XML_VALUE)
        .accept(MediaType.APPLICATION_XML_VALUE)
        .body(credentials)
        .when()
          .post()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_XML_VALUE)
        .extract()
          .body()
            .asString();

    token = objectMapper.readValue(content, TokenDTO.class);

    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }

  @Test
  @Order(2)
  void refreshToken() throws JsonProcessingException {
    var content = given()
        .basePath("/auth/refresh")
        .contentType(MediaType.APPLICATION_XML_VALUE)
        .accept(MediaType.APPLICATION_XML_VALUE)
        .pathParam("username", token.getUsername())
        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
        .when()
          .put("{username}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_XML_VALUE)
        .extract()
          .body()
            .asString();

    token = objectMapper.readValue(content, TokenDTO.class);

    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }
}
