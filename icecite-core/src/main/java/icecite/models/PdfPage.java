package icecite.models;

import java.util.List;

/**
 * A page in a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface PdfPage {
  /**
   * Returns the characters in this page.
   * 
   * @return The list of characters in this page.
   */
  List<PdfCharacter> getCharacters();

  /**
   * Sets the characters of this page.
   * 
   * @param characters
   *        The list of characters to set.
   */
  void setCharacters(List<PdfCharacter> characters);

  /**
   * Adds the given character to this page.
   * 
   * @param character
   *        The character to add.
   */
  void addCharacter(PdfCharacter character);

  // ==========================================================================
  
  /**
   * Returns the figures in this page.
   * 
   * @return The list of figures in this page.
   */
  List<PdfFigure> getFigures();

  /**
   * Sets the figures of this page.
   * 
   * @param figures
   *        The list of figures to set.
   */
  void setFigures(List<PdfFigure> figures);

  /**
   * Adds the given figure to this page.
   * 
   * @param figure
   *        The figure to add.
   */
  void addFigure(PdfFigure figure);
  
  // ==========================================================================
  
  /**
   * Returns the shapes in this page.
   * 
   * @return The list of shapes in this page.
   */
  List<PdfShape> getShapes();

  /**
   * Sets the shapes of this page.
   * 
   * @param shapes
   *        The list of shapes to set.
   */
  void setShapes(List<PdfShape> shapes);

  /**
   * Adds the given shape to this page.
   * 
   * @param shape
   *        The shape to add.
   */
  void addShape(PdfShape shape);
  
  // ==========================================================================

  /**
   * Returns the number of this page in the PDF document.
   * 
   * @return The page number.
   */
  int getPageNumber();

  /**
   * Sets the number of this page in the PDF document.
   * 
   * @param pageNumber
   *        The page number.
   */
  void setPageNumber(int pageNumber);
}
