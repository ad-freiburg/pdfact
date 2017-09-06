package pdfact.core.pipes.dehyphenate;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that dehyphenates words.
 * 
 * @author Claudius Korzen
 */
public interface DehyphenateWordsPipe extends Pipe {
  /**
   * The factory to create instances of {@link DehyphenateWordsPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface DehyphenateWordsPipeFactory {
    /**
     * Creates a new DehyphenateWordsPipe.
     * 
     * @return An instance of {@link DehyphenateWordsPipe}.
     */
    DehyphenateWordsPipe create();
  }
}