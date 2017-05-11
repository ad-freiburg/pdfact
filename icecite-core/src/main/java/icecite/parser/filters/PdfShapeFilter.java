package icecite.parser.filters;

import icecite.models.PdfShape;

/**
 * A class with methods to decide whether to filter PDF shapes while parsing
 * PDF files.
 * 
 * @author Claudius Korzen
 */
public class PdfShapeFilter {
  /**
   * Returns true if the given PDF shape should be filtered on parsing a PDF
   * document.
   * 
   * @param shape
   *        The shape to process.
   * @return true if the given PDF shape should be filtered on parsing a PDF
   *         document; false otherwise.
   */
  public static boolean filterPdfShape(PdfShape shape) {
    return false;
  }
}
