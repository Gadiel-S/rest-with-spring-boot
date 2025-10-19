package br.com.Gadiel_S.integrationtests.controllers.withyaml;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.Gadiel_S.integrationtests.dto.AccountCredentialsDTO;
import br.com.Gadiel_S.integrationtests.dto.TokenDTO;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

  private static YAMLMapper objectMapper;
  private static TokenDTO token;

  @BeforeAll
  static void setUp() {
    objectMapper = new YAMLMapper();
    token = new TokenDTO();
  }

  @Test
  @Order(1)
  void signIn() {
    AccountCredentialsDTO credentials = new AccountCredentialsDTO("gadiel", "admin123");

    token = given()
        .config(RestAssured.config()
          .encoderConfig(EncoderConfig.encoderConfig()
            .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
        .basePath("/auth/signin")
        .port(TestConfigs.SERVER_PORT)
        .contentType(MediaType.APPLICATION_YAML_VALUE)
        .accept(MediaType.APPLICATION_YAML_VALUE)
        .body(credentials, objectMapper)
        .when()
          .post()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(TokenDTO.class, objectMapper);

    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }

  @Test
  @Order(2)
  void refreshToken() {
    token = given()
        .config(RestAssured.config()
          .encoderConfig(EncoderConfig.encoderConfig()
            .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
        .basePath("/auth/refresh")
        .port(TestConfigs.SERVER_PORT)
        .contentType(MediaType.APPLICATION_YAML_VALUE)
        .accept(MediaType.APPLICATION_YAML_VALUE)
        .pathParam("username", token.getUsername())
        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
        .when()
          .put("{username}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(TokenDTO.class, objectMapper);

    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }
}
