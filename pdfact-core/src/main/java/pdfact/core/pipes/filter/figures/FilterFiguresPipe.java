package pdfact.core.pipes.filter.figures;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that filters those figures of a PDF document that should not be
 * considered.
 * 
 * @author Claudius Korzen
 */
public interface FilterFiguresPipe extends Pipe {
  /**
   * The factory to create instances of {@link FilterFiguresPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface FilterFiguresPipeFactory {
    /**
     * Creates a new instance of {@link FilterFiguresPipe}.
     * 
     * @return A new instance of {@link FilterFiguresPipe}.
     */
    FilterFiguresPipe create();
  }
}
