package pdfact.exception;

/**
 * The standard exception to throw on any visualization error.
 * 
 * @author Claudius Korzen
 */
public class PdfActVisualizeException extends PdfActException {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -2555647293109903637L;

  /**
   * The default exit code of this exception.
   */
  protected static final int EXIT_CODE = 40;

  /**
   * Creates a new exception.
   * 
   * @param message
   *        The message of this exception.
   */
  public PdfActVisualizeException(String message) {
    super(EXIT_CODE, message);
  }

  /**
   * Creates a new exception.
   * 
   * @param message
   *        The message of this exception.
   * @param cause
   *        The cause of this exception.
   */
  public PdfActVisualizeException(String message, Throwable cause) {
    super(EXIT_CODE, message, cause);
  }
}
