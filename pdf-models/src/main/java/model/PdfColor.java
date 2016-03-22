package model;

/**
 * The interface for a single pdf color.
 *
 * @author Claudius Korzen
 */
public interface PdfColor extends HasPdfDocument, HasId {  
  /**
   * Returns true, if this color is equal to white.
   */
  public boolean isWhite();

  /**
   * Returns true, if this color is equal to white, with respect to the given 
   * tolerance.
   */
  public boolean isWhite(float tolerance);
  
  /**
   * Returns the RGB array.
   */
  public float[] getRGB();
  
  /**
   * Returns the value of the red component.
   */
  public float getR();

  /**
   * Returns the value of the green component.
   */
  public float getG();

  /**
   * Returns the value of the blue component.
   */
  public float getB();
}
