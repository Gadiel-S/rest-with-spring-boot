package br.com.Gadiel_S.file.exporter.contract;

import br.com.Gadiel_S.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface PersonExporter {

  Resource exportPeople(List<PersonDTO> people) throws Exception;
  Resource exportPerson(PersonDTO person) throws Exception;
}
