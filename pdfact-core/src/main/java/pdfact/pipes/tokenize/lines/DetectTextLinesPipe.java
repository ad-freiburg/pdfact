package pdfact.pipes.tokenize.lines;

import pdfact.util.pipeline.Pipe;

/**
 * A pipe to tokenize the pages of a PDF document into text lines.
 * 
 * @author Claudius Korzen
 */
public interface DetectTextLinesPipe extends Pipe {
  /**
   * The factory to create instances of {@link DetectTextLinesPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface DetectTextLinesPipeFactory {
    /**
     * Creates a new instance of {@link DetectTextLinesPipe}.
     * 
     * @return A new instance of {@link DetectTextLinesPipe}.
     */
    DetectTextLinesPipe create();
  }
}
