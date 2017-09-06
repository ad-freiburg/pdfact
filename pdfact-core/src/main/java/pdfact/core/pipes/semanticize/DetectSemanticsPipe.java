package pdfact.core.pipes.semanticize;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe to semanticize the text blocks of a PDF document.
 * 
 * @author Claudius Korzen
 */
public interface DetectSemanticsPipe extends Pipe {
  /**
   * The factory to create instances of {@link DetectSemanticsPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface DetectSemanticsPipeFactory {
    /**
     * Creates a new SemanticizeTextBlocksPipe.
     * 
     * @return An instance of {@link DetectSemanticsPipe}.
     */
    DetectSemanticsPipe create();
  }
}
