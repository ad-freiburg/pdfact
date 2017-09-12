package pdfact.core.pipes.parse;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that parses the streams of a PDF file for characters, figures and
 * shapes.
 * 
 * @author Claudius Korzen
 */
public interface ParsePdfStreamsPipe extends Pipe {
  /**
   * The factory to create instances of {@link ParsePdfStreamsPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface ParsePdfPipeFactory {
    /**
     * Creates a new instance of {@link ParsePdfStreamsPipe}.
     * 
     * @return A new instance of {@link ParsePdfStreamsPipe}.
     */
    ParsePdfStreamsPipe create();
  }
}
