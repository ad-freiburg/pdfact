package pdfact.pipes.validate;

import pdfact.util.pipeline.Pipe;

/**
 * A pipe that validates paths to PDF files.
 * 
 * @author Claudius Korzen
 */
public interface ValidatePdfPathPipe extends Pipe {
  /**
   * The factory to create instances of {@link ValidatePdfPathPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface ValidatePdfPathPipeFactory {
    /**
     * Creates a new instance of {@link ValidatePdfPathPipe}.
     * 
     * @return A new instance of {@link ValidatePdfPathPipe}.
     */
    ValidatePdfPathPipe create();
  }
}
