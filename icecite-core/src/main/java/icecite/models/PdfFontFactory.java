package icecite.models;

/**
 * The factory to create instances of {@link PdfFont}.
 * 
 * @author Claudius Korzen
 */
public interface PdfFontFactory {
  /**
   * Creates a new PdfFont.
   * 
   * @return An instance of {@link PdfFont}.
   */
  PdfFont create();
}
