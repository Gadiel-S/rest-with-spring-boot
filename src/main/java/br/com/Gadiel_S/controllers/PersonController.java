package br.com.Gadiel_S.controllers;


import br.com.Gadiel_S.controllers.docs.PersonControllerDocs;
import br.com.Gadiel_S.data.dto.PersonDTO;
import br.com.Gadiel_S.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

  @Autowired
  private PersonServices service;

  @GetMapping(produces = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE,
      MediaType.APPLICATION_YAML_VALUE})
  @Override
  public List<PersonDTO> findAll() {
    return service.findAll();
  }

  @CrossOrigin(origins = "http://localhost:8080")
  @GetMapping(value = "/{id}",
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE}
  )
  @Override
  public PersonDTO findById(@PathVariable("id") Long id) {
    return service.findById(id);
  }

  @CrossOrigin(origins = {"http://localhost:8080", "https://github.com/Gadiel-S"})
  @PostMapping(
      consumes = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE},
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE}
  )
  @Override
  public PersonDTO create(@RequestBody PersonDTO person) {
    return service.create(person);
  }

  @PutMapping(
      consumes = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE},
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE}
  )
  @Override
  public PersonDTO update(@RequestBody PersonDTO person) {
    return service.update(person);
  }

  @PatchMapping(value = "/{id}",
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE}
  )
  @Override
  public PersonDTO disablePerson(@PathVariable("id") Long id) {
    return service.disablePerson(id);
  }

  @DeleteMapping(value = "/{id}")
  @Override
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
