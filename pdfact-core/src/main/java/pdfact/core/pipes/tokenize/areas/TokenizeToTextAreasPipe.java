package pdfact.core.pipes.tokenize.areas;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that tokenizes the pages of a PDF document into text areas.
 * 
 * @author Claudius Korzen
 */
public interface TokenizeToTextAreasPipe extends Pipe {
  /**
   * The factory to create instances of {@link TokenizeToTextAreasPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface TokenizeToTextAreasPipeFactory {
    /**
     * Creates a new instance of {@link TokenizeToTextAreasPipe}.
     * 
     * @return A new instance of {@link TokenizeToTextAreasPipe}.
     */
    TokenizeToTextAreasPipe create();
  }
}
