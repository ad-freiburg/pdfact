package pdfact.parse.filter;

import pdfact.models.PdfFigure;

/**
 * A class to filter out certain PDF figures while parsing PDF files.
 * 
 * @author Claudius Korzen
 */
public class PdfFigureFilter {
  /**
   * Checks if the given PDF figure should be filtered out.
   * 
   * @param figure
   *        The figure to check.
   * 
   * @return True if the given PDF figure should be filtered out; False
   *         otherwise.
   */
  public static boolean filterPdfFigure(PdfFigure figure) {
    return false;
  }
}
