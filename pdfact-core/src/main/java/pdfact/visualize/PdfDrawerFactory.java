package pdfact.visualize;

import java.io.File;
import java.nio.file.Path;

/**
 * The factory to create instances of {@link PdfDrawer}.
 * 
 * @author Claudius Korzen
 */
public interface PdfDrawerFactory {
  /**
   * Creates a new PdfDrawer.
   * 
   * @param pdf
   *        The PDF file to process.
   * 
   * @return A new instance of {@link PdfDrawer}.
   */
  PdfDrawer create(File pdf);

  /**
   * Creates a new PdfDrawer.
   * 
   * @param pdf
   *        The PDF file to process.
   * 
   * @return A new instance of {@link PdfDrawer}.
   */
  PdfDrawer create(Path pdf);
}
