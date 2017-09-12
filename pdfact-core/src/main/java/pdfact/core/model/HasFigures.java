package pdfact.core.model;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that have figures.
 *
 * @author Claudius Korzen
 */
public interface HasFigures {
  /**
   * Returns the figures of this element.
   * 
   * @return The figures.
   */
  List<Figure> getFigures();

  /**
   * Returns the first figure of this element.
   * 
   * @return The first figure or null if there are no figures.
   */
  Figure getFirstFigure();

  /**
   * Returns the last figure of the element.
   * 
   * @return The last figure or null if there are no figures.
   */
  Figure getLastFigure();

  // ==========================================================================

  /**
   * Sets the figures of this element.
   * 
   * @param figures
   *        The figures of this element.
   */
  void setFigures(List<Figure> figures);

  /**
   * Adds the given figures to this element.
   * 
   * @param figures
   *        The figures to add.
   */
  void addFigures(List<Figure> figures);

  /**
   * Adds the given figure to this element.
   * 
   * @param figure
   *        The figure to add.
   */
  void addFigure(Figure figure);
}