package pdfact.pipes.tokenize.blocks;

import pdfact.util.pipeline.Pipe;

/**
 * A pipe to tokenize the pages of a PDF document into text blocks.
 * 
 * @author Claudius Korzen
 */
public interface DetectTextBlocksPipe extends Pipe {
  /**
   * The factory to create instances of {@link DetectTextBlocksPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface DetectTextBlocksPipeFactory {
    /**
     * Creates a new TokenizeTextBlocksPipe.
     * 
     * @return An instance of {@link DetectTextBlocksPipe}.
     */
    DetectTextBlocksPipe create();
  }
}
