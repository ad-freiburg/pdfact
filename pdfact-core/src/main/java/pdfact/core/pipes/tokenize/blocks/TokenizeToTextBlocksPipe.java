package pdfact.core.pipes.tokenize.blocks;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that tokenizes the text lines of a PDF page into text blocks.
 * 
 * @author Claudius Korzen
 */
public interface TokenizeToTextBlocksPipe extends Pipe {
  /**
   * The factory to create instances of {@link TokenizeToTextBlocksPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface TokenizeToTextBlocksPipeFactory {
    /**
     * Creates a new instance of {@link TokenizeToTextBlocksPipe}.
     * 
     * @return A new instance of {@link TokenizeToTextBlocksPipe}.
     */
    TokenizeToTextBlocksPipe create();
  }
}
