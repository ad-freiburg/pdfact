package model;

/**
 * Any element.
 *
 * @author Claudius Korzen
 */
public interface PdfElement extends PdfArea, HasFeature {
  /**
   * Sets the role of this element.
   */
  public void setRole(PdfRole role);
  
  /**
   * Returns the role of this element.
   */
  public PdfRole getRole();
  
  /**
   * Returns the extraction order number.
   */
  public int getExtractionOrderNumber();
}
