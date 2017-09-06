package pdfact.core.pipes.tokenize.words;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that tokenize the text lines of a PDF page into words.
 * 
 * @author Claudius Korzen
 */
public interface TokenizeToWordsPipe extends Pipe {
  /**
   * The factory to create instances of {@link TokenizeToWordsPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface TokenizeToWordsPipeFactory {
    /**
     * Creates a new instance of {@link TokenizeToWordsPipe}.
     * 
     * @return A new instance of {@link TokenizeToWordsPipe}.
     */
    TokenizeToWordsPipe create();
  }
}
