package br.com.Gadiel_S.integrationtests.dto.wrappers.json;

import br.com.Gadiel_S.integrationtests.dto.BookDTO;
import br.com.Gadiel_S.integrationtests.dto.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class BookEmbeddedDTO implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @JsonProperty("books")
  private List<BookDTO> books;

  public BookEmbeddedDTO() {
  }

  public List<BookDTO> getBooks() {
    return books;
  }

  public void setBooks(List<BookDTO> books) {
    this.books = books;
  }
}
