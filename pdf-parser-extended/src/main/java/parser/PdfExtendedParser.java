package parser;

import model.PdfDocument;

/**
 * The super class for all concrete implementations of a PdfParser. 
 *
 * @author Claudius Korzen
 */
public interface PdfExtendedParser {

  /**
   * Parses the given document.
   */
  public PdfDocument parse(PdfDocument document);
}
