package icecite.models;

/**
 * A font in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfFont extends PdfResource {
  /**
   * Returns the id of this font.
   * 
   * @return The id of this font.
   */
  String getId();
  
  /**
   * Sets the id of this font.
   * 
   * @param name The id of this font.
   */
  void setId(String name);
  
  // ==========================================================================
  
  /**
   * Returns the full name of this font.
   * 
   * @return The full name of this font.
   */
  String getName();
  
  /**
   * Sets the full name of this font.
   * 
   * @param name The full name of this font.
   */
  void setName(String name);
  
  // ==========================================================================
  
  /**
   * Returns the base name of this font.
   * 
   * @return The base name of this font.
   */
  String getBaseName();
  
  /**
   * Sets the base name of this font.
   * 
   * @param name The base name of this font.
   */
  void setBaseName(String name);
  
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
