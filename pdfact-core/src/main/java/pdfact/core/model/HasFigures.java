package pdfact.core.model;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that have figures.
 *
 * @author Claudius Korzen
 */
public interface HasFigures {
  /**
   * Returns the figures of the element.
   * 
   * @return The figures.
   */
  List<Figure> getFigures();

  /**
   * Returns the first figure of the element.
   * 
   * @return The first figure.
   */
  Figure getFirstFigure();

  /**
   * Returns the last figure of the element.
   * 
   * @return The last figure.
   */
  Figure getLastFigure();

  // ==========================================================================

  /**
   * Sets the figures of the element.
   * 
   * @param figures
   *        The figures to set.
   */
  void setFigures(List<Figure> figures);

  /**
   * Adds the given figures to the element.
   * 
   * @param figures
   *        The figures to add.
   */
  void addFigures(List<Figure> figures);

  /**
   * Adds the given figure to the element.
   * 
   * @param figure
   *        The figure to add.
   */
  void addFigure(Figure figure);
}