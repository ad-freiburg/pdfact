package pdfact.pipes.filter.shapes;

import pdfact.util.pipeline.Pipe;

/**
 * A pipe that filters those shapes of a PDF document that should not be
 * considered.
 * 
 * @author Claudius Korzen
 */
public interface FilterShapesPipe extends Pipe {
  /**
   * The factory to create instances of {@link FilterShapesPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface FilterShapesPipeFactory {
    /**
     * Creates a new instance of {@link FilterShapesPipe}.
     * 
     * @return A new instance of {@link FilterShapesPipe}.
     */
    FilterShapesPipe create();
  }
}