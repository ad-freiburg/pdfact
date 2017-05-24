package icecite.models.plain;

import icecite.models.PdfFont;

/**
 * A plain implementation of {@link PdfFont}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfFont implements PdfFont {
  /**
   * The id of this font.
   */
  protected String id;

  /**
   * The full name of this font.
   */
  protected String name;

  /**
   * The base name of this font.
   */
  protected String basename;

  // ==========================================================================

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  // ==========================================================================

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  // ==========================================================================

  @Override
  public String getBaseName() {
    return this.basename;
  }

  @Override
  public void setBaseName(String name) {
    this.basename = name;
  }
}
