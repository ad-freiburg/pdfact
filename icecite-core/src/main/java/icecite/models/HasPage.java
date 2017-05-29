package icecite.models;

/**
 * An interface that declares that the implementing object has a PDF page.
 *
 * @author Claudius Korzen
 */
public interface HasPage {
  /**
   * Returns the page in which this element is located.
   * 
   * @return The page in which this element is located.
   */
  PdfPage getPage();

  /**
   * Sets the page in which this element is located.
   * 
   * @param page
   *        The page in which this element is located.
   */
  void setPage(PdfPage page);
}
