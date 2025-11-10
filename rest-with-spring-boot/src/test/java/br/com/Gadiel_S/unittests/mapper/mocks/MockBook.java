package br.com.Gadiel_S.unittests.mapper.mocks;

import br.com.Gadiel_S.data.dto.BookDTO;
import br.com.Gadiel_S.models.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(new Date(1234567890000L));
        book.setPrice(100.00 + number);
        book.setTitle("Title Test" + number);
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(new Date(1234567890000L));
        book.setPrice(100.00 + number);
        book.setTitle("Title Test" + number);
        return book;
    }

}