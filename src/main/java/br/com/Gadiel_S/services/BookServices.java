package br.com.Gadiel_S.services;

import br.com.Gadiel_S.controllers.BookController;
import br.com.Gadiel_S.data.dto.BookDTO;
import static br.com.Gadiel_S.mapper.ObjectMapper.parseListObjects;

import br.com.Gadiel_S.exceptions.RequiredObjectIsNullException;
import br.com.Gadiel_S.exceptions.ResourceNotFoundException;
import br.com.Gadiel_S.models.Book;
import br.com.Gadiel_S.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static br.com.Gadiel_S.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServices {

  private Logger logger = LoggerFactory.getLogger((BookServices.class.getName()));

  @Autowired
  BookRepository repository;

  public List<BookDTO> findAll() {
    logger.info("Finding all Books!");
    var books = parseListObjects(repository.findAll(), BookDTO.class);
    books.forEach(this::addHateoasLinks);
    return books;
  }

  public BookDTO findById(Long id) {
    logger.info("Finding one Book!");
    var entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    var dto = parseObject(entity, BookDTO.class);
    addHateoasLinks(dto);
    return dto;
  }

  public BookDTO create(BookDTO book) {
    if(book==null) throw new RequiredObjectIsNullException();
    logger.info("Creating one Book!");
    var entity = parseObject(book, Book.class);
    var dto = parseObject(repository.save(entity), BookDTO.class);
    addHateoasLinks(dto);
    return dto;
  }

  public BookDTO update(BookDTO book) {
    if(book==null) throw new RequiredObjectIsNullException();
    logger.info("Updating one Book!");
    Book entity = repository.findById(book.getId())
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    entity.setAuthor(book.getAuthor());
    entity.setLaunchDate(book.getLaunchDate());
    entity.setPrice(book.getPrice());
    entity.setTitle(book.getTitle());
    var dto = parseObject(repository.save(entity), BookDTO.class);
    addHateoasLinks(dto);
    return dto;
  }

  public void delete(Long id) {
    logger.info("Deleting one Book!");
    Book entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    repository.delete(entity);
  }

  private void addHateoasLinks(BookDTO dto) {
    dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
    dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
    dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
    dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
    dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
  }
}
