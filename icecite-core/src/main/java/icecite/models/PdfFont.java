package icecite.models;

/**
 * A font in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfFont extends PdfResource {

  // ==========================================================================

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
}
