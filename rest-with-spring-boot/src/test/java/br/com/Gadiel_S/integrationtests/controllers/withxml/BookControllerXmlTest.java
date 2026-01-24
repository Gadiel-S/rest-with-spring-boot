package br.com.Gadiel_S.integrationtests.controllers.withxml;

import br.com.Gadiel_S.config.TestConfigs;
import br.com.Gadiel_S.integrationtests.dto.AccountCredentialsDTO;
import br.com.Gadiel_S.integrationtests.dto.BookDTO;
import br.com.Gadiel_S.integrationtests.dto.TokenDTO;
import br.com.Gadiel_S.integrationtests.dto.wrappers.xmlAndYaml.PagedModelBook;
import br.com.Gadiel_S.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerXmlTest extends AbstractIntegrationTest {

  @LocalServerPort
  private int port;

  private static RequestSpecification specification;
  private static XmlMapper objectMapper;
  private static BookDTO book;
  private static TokenDTO token;

  @BeforeAll
  static void setUp() {
    objectMapper = new XmlMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    book = new BookDTO();
    token = new TokenDTO();
  }

  @BeforeEach
  void setUpEach() {
    RestAssured.port = port;
  }

  @Test
  @Order(0)
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

    specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GITHUB)
        .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
        .setBasePath("/api/book/v1")
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();

    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }

  @Test
  @Order(1)
  void createTest() throws JsonProcessingException {
    mockBook();

    var content = given(specification)
        .contentType(MediaType.APPLICATION_XML_VALUE)
        .accept(MediaType.APPLICATION_XML_VALUE)
          .body(book)
        .when()
          .post()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_XML_VALUE)
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
        .contentType(MediaType.APPLICATION_XML_VALUE)
        .accept(MediaType.APPLICATION_XML_VALUE)
          .body(book)
        .when()
          .put()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_XML_VALUE)
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
        .accept(MediaType.APPLICATION_XML_VALUE)
          .pathParam("id", book.getId())
        .when()
          .get("{id}")
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_XML_VALUE)
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
        .accept(MediaType.APPLICATION_XML_VALUE)
        .queryParams("page", 3, "size", 12, "direction", "asc")
        .when()
          .get()
        .then()
          .statusCode(200)
          .contentType(MediaType.APPLICATION_XML_VALUE)
        .extract()
          .body()
            .asString();

    PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
    List<BookDTO> book = wrapper.getContent();

    BookDTO bookOne = book.getFirst();

    assertNotNull(bookOne.getId());
    assertTrue(bookOne.getId() > 0);

    assertEquals("Eric Freeman, Elisabeth Freeman, Kathy Sierra, Bert Bates", bookOne.getAuthor());
    assertNotNull(bookOne.getLaunchDate());
    assertEquals(110.0, bookOne.getPrice());
    assertEquals("Head First Design Patterns", bookOne.getTitle());

    BookDTO bookThree = book.get(2);

    assertNotNull(bookThree.getId());
    assertTrue(bookThree.getId() > 0);

    assertEquals("Brian Goetz e Tim Peierls", bookThree.getAuthor());
    assertNotNull(bookThree.getLaunchDate());
    assertEquals(80.0, bookThree.getPrice());
    assertEquals("Java Concurrency in Practice", bookThree.getTitle());
  }

  private void mockBook() {
    book.setAuthor("Linus Torvalds");
    book.setLaunchDate(new Date());
    book.setPrice(120.50);
    book.setTitle("A very interesting book");
  }
}