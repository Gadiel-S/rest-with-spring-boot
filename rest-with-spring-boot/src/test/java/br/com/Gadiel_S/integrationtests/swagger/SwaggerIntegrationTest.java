package br.com.Gadiel_S.integrationtests.swagger;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUpEach() {
		RestAssured.port = port;
	}

	@Test
	void shouldDisplaySwaggerUIPage() {
		var content = given()
				.basePath("/swagger-ui/index.html")
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		assertTrue(content.contains("Swagger UI"));
	}

}
