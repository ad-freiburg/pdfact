package icecite.models;

/**
 * The factory to create instances of {@link PdfColor}.
 * 
 * @author Claudius Korzen
 */
public interface PdfColorFactory {
  /**
   * Creates a PdfColor.
   * 
   * @return An instance of {@link PdfColor}.
   */
  PdfColor create();

  /**
   * Creates a PdfColor.
   * 
   * @param rgb
   *        The RGB value of this color.
   * 
   * @return An instance of {@link PdfColor}.
   */
  PdfColor create(float[] rgb);
}
