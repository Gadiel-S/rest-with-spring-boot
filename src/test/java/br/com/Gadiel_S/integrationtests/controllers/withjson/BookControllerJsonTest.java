package br.com.Gadiel_S.integrationtests.controllers.withjson;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.dto.BookDTO;
import br.com.Gadiel_S.integrationtests.dto.wrappers.json.WrapperBookDTO;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
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
class BookControllerJsonTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;
  private static BookDTO book;

  @BeforeAll
  static void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    book = new BookDTO();
  }

  @Test
  @Order(1)
  void createTest() throws JsonProcessingException {
    mockBook();
    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
        .setBasePath("/api/book/v1")
        .setPort(TestConfigs.SERVER_PORT)
          .addFilter(new RequestLoggingFilter(LogDetail.ALL))
          .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(book)
        .when()
          .post()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
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
  void updateTest() throws JsonProcessingException {
    book.setPrice(140.50);

    var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(book)
        .when()
          .put()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
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
  void findByIdTest() throws JsonProcessingException {
    var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
          .pathParam("id", book.getId())
        .when()
          .get("{id}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
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
  void findAllTest() throws JsonProcessingException {
    var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .queryParams("page", 0, "size", 12, "direction", "asc")
        .when()
          .get()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
        .extract()
          .body()
            .asString();

    WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);
    List<BookDTO> book = wrapper.getEmbedded().getBooks();

    BookDTO bookOne = book.getFirst();

    assertNotNull(bookOne.getId());
    assertTrue(bookOne.getId() > 0);

    assertEquals("Craig Larman", bookOne.getAuthor());
    assertNotNull(bookOne.getLaunchDate());
    assertEquals(144.98, bookOne.getPrice());
    assertEquals("Agile and Iterative Development: A Manager’s Guide", bookOne.getTitle());

    BookDTO bookThree = book.get(2);

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