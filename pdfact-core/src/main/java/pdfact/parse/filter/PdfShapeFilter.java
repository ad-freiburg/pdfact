package pdfact.parse.filter;

import pdfact.models.PdfShape;

/**
 * A class to filter out certain PDF shapes while parsing PDF files.
 * 
 * @author Claudius Korzen
 */
public class PdfShapeFilter {
  /**
   * Checks if the given shape should be filtered out.
   * 
   * @param shape
   *        The shape to check.
   * 
   * @return True if the given PDF shape should be filtered out; false
   *         otherwise.
   */
  public static boolean filterPdfShape(PdfShape shape) {
    return false;
  }
}
