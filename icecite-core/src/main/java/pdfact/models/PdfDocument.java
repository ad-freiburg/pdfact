package pdfact.models;

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
  List<PdfPage> getPages();

  /**
   * Sets the pages of this PDF document.
   * 
   * @param pages
   *        The pages to set.
   */
  void setPages(List<PdfPage> pages);

  /**
   * Adds the given page to this PDF document.
   * 
   * @param page
   *        The page to add.
   */
  void addPage(PdfPage page);

  // ==========================================================================

  /**
   * Returns the path to the underlying PDF file as a File object.
   * 
   * @return The path to the underlying PDF file as a File object.
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
   * Returns the path to the underlying PDF file as a Path object.
   * 
   * @return The path to the underlying PDF file as a Path object.
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
     * Creates a new instance of PdfDocument.
     * 
     * @param pdf
     *        The path to the underlying PDF file.
     * 
     * @return A new instance of {@link PdfDocument}.
     */
    PdfDocument create(File pdf);

    /**
     * Creates a new instance of PdfDocument.
     * 
     * @param pdf
     *        The path to the underlying PDF file.
     * 
     * @return A new instance of {@link PdfDocument}.
     */
    PdfDocument create(Path pdf);
  }
}
