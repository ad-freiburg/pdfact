package icecite.models;

/**
 * The factory to creates instances of {@link PdfPage}.
 * 
 * @author Claudius Korzen
 */
public interface PdfPageFactory {
  /**
   * Creates a new PDF page.
   * 
   * @return An instance of {@link PdfPage}.
   */
  PdfPage create();

  /**
   * Creates a new PDF page.
   * 
   * @param pageNum
   *        The number of the page in the PDF document.
   * 
   * @return An instance of {@link PdfPage}.
   */
  PdfPage create(int pageNum);
}
