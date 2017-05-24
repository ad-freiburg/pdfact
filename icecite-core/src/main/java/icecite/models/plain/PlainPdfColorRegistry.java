package icecite.models.plain;

import java.util.HashMap;
import java.util.Map;

import icecite.models.PdfColor;
import icecite.models.PdfColorRegistry;

/**
 * A plain implementation of PdfColorFactory.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfColorRegistry implements PdfColorRegistry {
  /**
   * The index of PdfColor objects per name.
   */
  protected Map<String, PdfColor> index = new HashMap<>();

  @Override
  public boolean hasColor(String colorName) {
    return this.index.containsKey(colorName);
  }

  @Override
  public PdfColor getColor(String colorName) {
    return this.index.get(colorName);
  }

  @Override
  public void registerColor(PdfColor color) {
    if (color == null) {
      return;
    }
    String colorName = color.getName();
    if (colorName == null) {
      return;
    }
    if (hasColor(colorName)) {
      throw new IllegalStateException("The color is already registered.");
    }
    // Set the id of the color.
    color.setId("" + this.index.size());
    // Index the color.
    this.index.put(colorName, color);
  }
}
