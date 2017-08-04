package pdfact.models;

import java.util.List;

/**
 * An interface that is implemented by PDF elements that consist of figures.
 *
 * @author Claudius Korzen
 */
public interface HasFigures {
  /**
   * Returns the figures of the element.
   * 
   * @return The figures.
   */
  List<PdfFigure> getFigures();

  /**
   * Returns the first figure of the element.
   * 
   * @return The first figure.
   */
  PdfFigure getFirstFigure();

  /**
   * Returns the last figure of the element.
   * 
   * @return The last figure.
   */
  PdfFigure getLastFigure();

  // ==========================================================================

  /**
   * Sets the figures of the element.
   * 
   * @param figures
   *        The figures to set.
   */
  void setFigures(List<PdfFigure> figures);

  /**
   * Adds figures to the element.
   * 
   * @param figures
   *        The figures to add.
   */
  void addFigures(List<PdfFigure> figures);

  /**
   * Adds the given figure to the element.
   * 
   * @param figure
   *        The figure to add.
   */
  void addFigure(PdfFigure figure);
}