package pdfact.pipes.filter.characters;

import pdfact.util.pipeline.Pipe;

/**
 * A pipe that filters those characters of a PDF document that should not be
 * considered.
 * 
 * @author Claudius Korzen
 */
public interface FilterCharactersPipe extends Pipe {
  /**
   * The factory to create instances of {@link FilterCharactersPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface FilterCharactersPipeFactory {
    /**
     * Creates a new instance of {@link FilterCharactersPipe}.
     * 
     * @return A new instance of {@link FilterCharactersPipe}.
     */
    FilterCharactersPipe create();
  }
}
