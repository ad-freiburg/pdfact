package pdfact.pipes.tokenize.lines;

import pdfact.util.pipeline.Pipe;

/**
 * A pipe that tokenize the text areas of a PDF page into text lines.
 * 
 * @author Claudius Korzen
 */
public interface TokenizeToTextLinesPipe extends Pipe {
  /**
   * The factory to create instances of {@link TokenizeToTextLinesPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface TokenizeToTextLinesPipeFactory {
    /**
     * Creates a new instance of {@link TokenizeToTextLinesPipe}.
     * 
     * @return A new instance of {@link TokenizeToTextLinesPipe}.
     */
    TokenizeToTextLinesPipe create();
  }
}
