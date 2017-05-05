package icecite.models;

import java.io.File;
import java.nio.file.Path;

/**
 * The factory to create instances of {@link PdfDocument}.
 * 
 * @author Claudius Korzen
 */
public interface PdfDocumentFactory {
  /**
   * Creates a PdfDocument.
   * 
   * @return An instance of {@link PdfDocument}.
   */
  PdfDocument create();

  /**
   * Creates a PdfDocument.
   * 
   * @param pdf
   *        The file on which the PDF document is based on.
   * 
   * @return An instance of {@link PdfDocument}.
   */
  PdfDocument create(File pdf);

  /**
   * Creates a PdfDocument.
   * 
   * @param pdf
   *        The file on which the PDF document is based on.
   * 
   * @return An instance of {@link PdfDocument}.
   */
  PdfDocument create(Path pdf);
}
