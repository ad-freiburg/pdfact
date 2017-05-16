package icecite.models;

/**
 * A registry to manage PdfFont objects.
 * 
 * @author Claudius Korzen
 */
public interface PdfFontRegistry {
  /**
   * Checks if there is a PDF font registered for the given font name.
   * 
   * @param fontName
   *        The name of the font.
   * 
   * @return True, if there is a PDF font registered for the given font name;
   *         False otherwise.
   */
  boolean hasFont(String fontName);

  /**
   * Returns a PdfFont object for the given fontName.
   * 
   * @param fontName
   *        The name of the font.
   * 
   * @return An instance of {@link PdfFont}.
   */
  PdfFont getFont(String fontName);
  
  /**
   * Registers the given font.
   * 
   * @param font
   *        The font to register.
   */
  void registerFont(PdfFont font);
}
