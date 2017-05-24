package icecite.models;

import java.util.Set;

/**
 * An interface that declares that the implementing object consists of multiple
 * figures.
 *
 * @author Claudius Korzen
 */
public interface HasFigures {
  /**
   * Returns the set of figures.
   * 
   * @return The set of figures.
   */
  Set<PdfFigure> getFigures();

  /**
   * Sets the figures.
   * 
   * @param figures
   *        The figures to set.
   */
  void setFigures(Set<PdfFigure> figures);
  
  /**
   * Adds the given figure.
   * 
   * @param figure
   *        The figure to add.
   */
  void addFigure(PdfFigure figure);
}