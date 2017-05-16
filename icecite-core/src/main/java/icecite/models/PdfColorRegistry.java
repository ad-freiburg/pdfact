package icecite.models;

/**
 * The registry to manage PdfColor objects.
 * 
 * @author Claudius Korzen
 */
public interface PdfColorRegistry {
  /**
   * Checks if there is a PDF color registered for the given color name.
   * 
   * @param colorName
   *        The name of the color.
   * 
   * @return True, if there is a PDF color registered for the given color name;
   *         False otherwise.
   */
  boolean hasColor(String colorName);

  /**
   * Returns a PdfColor object for the given colorName.
   * 
   * @param colorName
   *        The name of the color.
   * 
   * @return An instance of {@link PdfColor}.
   */
  PdfColor getColor(String colorName);
  
  /**
   * Registers the given color.
   * 
   * @param color
   *        The color to register.
   */
  void registerColor(PdfColor color);
}
