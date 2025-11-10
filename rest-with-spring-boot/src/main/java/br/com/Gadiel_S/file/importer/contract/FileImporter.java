package br.com.Gadiel_S.file.importer.contract;

import br.com.Gadiel_S.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

  List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
