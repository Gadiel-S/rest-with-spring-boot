package br.com.Gadiel_S.integrationtests.controllers.withyaml;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.Gadiel_S.integrationtests.dto.BookDTO;
import br.com.Gadiel_S.integrationtests.dto.wrappers.xmlAndYaml.PagedModelBook;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerYamlTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static YAMLMapper objectMapper;
  private static BookDTO book;

  @BeforeAll
  static void setUp() {
    objectMapper = new YAMLMapper();
    book = new BookDTO();
  }

  @Test
  @Order(1)
  void createTest() {
    mockBook();
    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
        .setBasePath("/api/book/v1")
        .setPort(TestConfigs.SERVER_PORT)
          .addFilter(new RequestLoggingFilter(LogDetail.ALL))
          .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
          .setConfig(RestAssured.config()
            .encoderConfig(EncoderConfig.encoderConfig()
              .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
        .build();

    var createdBook = given(specification)
        .contentType(MediaType.APPLICATION_YAML_VALUE)
        .accept(MediaType.APPLICATION_YAML_VALUE)
          .body(book, objectMapper)
        .when()
          .post()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(BookDTO.class, objectMapper);

    book = createdBook;

    assertNotNull(createdBook.getId());
    assertTrue(createdBook.getId() > 0);

    assertEquals("Linus Torvalds", createdBook.getAuthor());
    assertNotNull(createdBook.getLaunchDate());
    assertEquals(120.50, createdBook.getPrice());
    assertEquals("A very interesting book", createdBook.getTitle());
  }

  @Test
  @Order(2)
  void updateTest() {
    book.setPrice(140.50);

    var createdBook = given(specification)
        .contentType(MediaType.APPLICATION_YAML_VALUE)
        .accept(MediaType.APPLICATION_YAML_VALUE)
          .body(book, objectMapper)
        .when()
          .put()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(BookDTO.class, objectMapper);

    book = createdBook;

    assertNotNull(createdBook.getId());
    assertTrue(createdBook.getId() > 0);

    assertEquals("Linus Torvalds", createdBook.getAuthor());
    assertNotNull(createdBook.getLaunchDate());
    assertEquals(140.50, createdBook.getPrice());
    assertEquals("A very interesting book", createdBook.getTitle());
  }

  @Test
  @Order(3)
  void findByIdTest() {
    var createdBook = given(specification)
        .accept(MediaType.APPLICATION_YAML_VALUE)
          .pathParam("id", book.getId())
        .when()
          .get("{id}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(BookDTO.class, objectMapper);

    book = createdBook;

    assertNotNull(createdBook.getId());
    assertTrue(createdBook.getId() > 0);

    assertEquals("Linus Torvalds", createdBook.getAuthor());
    assertNotNull(createdBook.getLaunchDate());
    assertEquals(140.50, createdBook.getPrice());
    assertEquals("A very interesting book", createdBook.getTitle());
  }

  @Test
  @Order(4)
  void deleteTest() {
    given(specification)
          .pathParam("id", book.getId())
        .when()
          .delete("{id}")
        .then()
          .statusCode(204);
  }

  @Test
  @Order(5)
  void findAllTest() {
    var response = given(specification)
        .accept(MediaType.APPLICATION_YAML_VALUE)
        .queryParams("page", 0, "size", 12, "direction", "asc")
        .when()
          .get()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_YAML_VALUE)
        .extract()
          .body()
            .as(PagedModelBook.class, objectMapper);

    List<BookDTO> books = response.getContent();

    BookDTO bookOne = books.getFirst();

    assertNotNull(bookOne.getId());
    assertTrue(bookOne.getId() > 0);

    assertEquals("Craig Larman", bookOne.getAuthor());
    assertNotNull(bookOne.getLaunchDate());
    assertEquals(144.98, bookOne.getPrice());
    assertEquals("Agile and Iterative Development: A Manager’s Guide", bookOne.getTitle());

    BookDTO bookThree = books.get(2);

    assertNotNull(bookThree.getId());
    assertTrue(bookThree.getId() > 0);

    assertEquals("Craig Larman", bookThree.getAuthor());
    assertNotNull(bookThree.getLaunchDate());
    assertEquals(72.89, bookThree.getPrice());
    assertEquals("Agile and Iterative Development: A Manager’s Guide", bookThree.getTitle());
  }

  private void mockBook() {
    book.setAuthor("Linus Torvalds");
    book.setLaunchDate(new Date());
    book.setPrice(120.50);
    book.setTitle("A very interesting book");
  }
}