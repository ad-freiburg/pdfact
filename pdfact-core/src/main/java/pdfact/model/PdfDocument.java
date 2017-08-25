package pdfact.model;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * A PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfDocument extends HasParagraphs {
  /**
   * Returns the pages of this PDF document.
   * 
   * @return The pages of this PDF document.
   */
  List<Page> getPages();

  /**
   * Returns the first page of this PDF document.
   * 
   * @return The first page of this PDF document.
   */
  Page getFirstPage();

  /**
   * Returns the last page of this PDF document.
   * 
   * @return The last page of this PDF document.
   */
  Page getLastPage();

  // ==========================================================================

  /**
   * Sets the pages of this PDF document.
   * 
   * @param pages
   *        The pages to set.
   */
  void setPages(List<Page> pages);

  /**
   * Adds the given page to this PDF document.
   * 
   * @param page
   *        The page to add.
   */
  void addPage(Page page);

  // ==========================================================================

  /**
   * Returns the path to the underlying PDF file.
   * 
   * @return The path to the underlying PDF file.
   */
  File getFile();

  /**
   * Sets the path to the underlying PDF file.
   * 
   * @param file
   *        The path to the underlying PDF file.
   */
  void setFile(File file);

  // ==========================================================================

  /**
   * Returns the path to the underlying PDF file.
   * 
   * @return The path to the underlying PDF file.
   */
  Path getPath();

  /**
   * Sets the path to the underlying PDF file.
   * 
   * @param path
   *        The path to the underlying PDF file.
   */
  void setPath(Path path);

  // ==========================================================================

  /**
   * The factory to create instances of {@link PdfDocument}.
   * 
   * @author Claudius Korzen
   */
  public interface PdfDocumentFactory {
    /**
     * Creates a new instance of {@link PdfDocument}.
     * 
     * @param path
     *        The path to the underlying PDF file.
     * 
     * @return A new instance of {@link PdfDocument}.
     */
    PdfDocument create(String path);
    
    /**
     * Creates a new instance of {@link PdfDocument}.
     * 
     * @param path
     *        The path to the underlying PDF file.
     * 
     * @return A new instance of {@link PdfDocument}.
     */
    PdfDocument create(File path);

    /**
     * Creates a new instance of {@link PdfDocument}.
     * 
     * @param path
     *        The path to the underlying PDF file.
     * 
     * @return A new instance of {@link PdfDocument}.
     */
    PdfDocument create(Path path);
  }
}
