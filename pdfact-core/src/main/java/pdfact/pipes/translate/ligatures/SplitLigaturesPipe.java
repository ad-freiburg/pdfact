package pdfact.pipes.translate.ligatures;

import pdfact.util.pipeline.Pipe;

/**
 * A pipe that splits ligatures.
 * 
 * @author Claudius Korzen
 */
public interface SplitLigaturesPipe extends Pipe {
  /**
   * The factory to create instances of {@link SplitLigaturesPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface SplitLigaturesPipeFactory {
    /**
     * Creates a new instance of {@link SplitLigaturesPipe}.
     * 
     * @return A new instance of {@link SplitLigaturesPipe}.
     */
    SplitLigaturesPipe create();
  }
}
