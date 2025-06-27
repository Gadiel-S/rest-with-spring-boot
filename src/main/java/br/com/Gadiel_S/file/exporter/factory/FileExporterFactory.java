package br.com.Gadiel_S.file.exporter.factory;

import br.com.Gadiel_S.exceptions.BadRequestException;
import br.com.Gadiel_S.file.exporter.MediaTypes;
import br.com.Gadiel_S.file.exporter.contract.FileExporter;
import br.com.Gadiel_S.file.exporter.impl.CsvExporter;
import br.com.Gadiel_S.file.exporter.impl.XlsxExporter;
import br.com.Gadiel_S.file.importer.impl.CsvImporter;
import br.com.Gadiel_S.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

  private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

  @Autowired
  private ApplicationContext context;

  public FileExporter getExporter(String acceptHeader) throws Exception {
    if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
      return context.getBean(XlsxExporter.class);
    } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
      return context.getBean(CsvExporter.class);
    } else {
      throw new BadRequestException("Invalid File Format!");
    }
  }
}
