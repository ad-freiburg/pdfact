package pdfact.core.pipes.translate.diacritics;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that merges characters with related diacritical marks.
 * 
 * @author Claudius Korzen
 */
public interface MergeDiacriticsPipe extends Pipe {
  /**
   * The factory to create instances of {@link MergeDiacriticsPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface MergeDiacriticsPipeFactory {
    /**
     * Creates a new {@link MergeDiacriticsPipe}.
     * 
     * @return An instance of {@link MergeDiacriticsPipe}.
     */
    MergeDiacriticsPipe create();
  }
}
