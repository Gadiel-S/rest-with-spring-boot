package br.com.Gadiel_S.integrationtests.controllers.withjson;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.dto.AccountCredentialsDTO;
import br.com.Gadiel_S.integrationtests.dto.TokenDTO;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerJsonTest extends AbstractIntegrationTest {

  @LocalServerPort
  private int port;

  private static TokenDTO token;

  @BeforeAll
  static void setUp() {
    token = new TokenDTO();
  }

  @BeforeEach
  void setUpEach() {
    RestAssured.port = port;
  }

  @Test
  @Order(1)
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
  @Order(2)
  void refreshToken() {
    token = given()
        .basePath("/auth/refresh")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("username", token.getUsername())
        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
        .when()
          .put("{username}")
        .then()
          .statusCode(200)
        .extract()
          .body()
            .as(TokenDTO.class);

    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }
}