package icecite.models;

/**
 * An interface that declares that the implementing object belongs to a PDF
 * page.
 *
 * @author Claudius Korzen
 */
public interface HasPage {
  /**
   * Returns the PDF page.
   * 
   * @return The PDF page.
   */
  PdfPage getPage();

  /**
   * Sets the given PDF page.
   * 
   * @param page
   *        The PDF page.
   */
  void setPage(PdfPage page);
}
