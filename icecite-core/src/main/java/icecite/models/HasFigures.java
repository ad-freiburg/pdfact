package icecite.models;

import java.util.List;

/**
 * An interface that declares that the implementing object has figures.
 *
 * @author Claudius Korzen
 */
public interface HasFigures {
  /**
   * Returns the figures.
   * 
   * @return The figures.
   */
  List<PdfFigure> getFigures();

  /**
   * Returns the first figure.
   * 
   * @return The first figure.
   */
  PdfFigure getFirstFigure();

  /**
   * Returns the last figure.
   * 
   * @return The last figure.
   */
  PdfFigure getLastFigure();

  // ==========================================================================

  /**
   * Sets the given figures.
   * 
   * @param figures
   *        The figures.
   */
  void setFigures(List<PdfFigure> figures);

  /**
   * Adds the given figures.
   * 
   * @param figures
   *        The figures.
   */
  void addFigures(List<PdfFigure> figures);

  /**
   * Adds the given figure.
   * 
   * @param figure
   *        The figure.
   */
  void addFigure(PdfFigure figure);
}