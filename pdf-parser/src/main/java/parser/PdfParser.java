package parser;

import java.io.IOException;
import java.nio.file.Path;

import model.PdfDocument;

/**
 * The interface of a common pdf parser.
 *
 * @author Claudius Korzen
 */
public interface PdfParser {
  /**
   * Parses the given pdf file and returns the produced PdfDocument.
   */
  public PdfDocument parse(Path file) throws IOException;

  /**
   * Returns the name of this parser.
   */
  public String getName();
}