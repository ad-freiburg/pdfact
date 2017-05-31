package icecite.models;

/**
 * An interface that declares that the implementing object belongs to a PDF
 * page.
 *
 * @author Claudius Korzen
 */
public interface HasPage {
  /**
   * Returns the page.
   * 
   * @return The page.
   */
  PdfPage getPage();

  /**
   * Sets the given page.
   * 
   * @param page
   *        The page.
   */
  void setPage(PdfPage page);
}
