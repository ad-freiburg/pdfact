package icecite.parse.filter;

import icecite.models.PdfFigure;

/**
 * A class with methods to decide whether to filter PDF figures while parsing
 * PDF files.
 * 
 * @author Claudius Korzen
 */
public class PdfFigureFilter {
  /**
   * Returns true if the given PDF figure should be filtered on parsing a PDF
   * document.
   * 
   * @param figure
   *        The figure to process.
   * @return true if the given PDF figure should be filtered on parsing a PDF
   *         document; false otherwise.
   */
  public static boolean filterPdfFigure(PdfFigure figure) {
    return false;
  }
}
