package pdfact.exception;

/**
 * The standard exception to throw on any errors on parsing a PDF.
 * 
 * @author Claudius Korzen
 */
public class PdfActParseException extends PdfActException {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = 3552957003556580621L;

  /**
   * The default exit code of this exception.
   */
  protected static final int EXIT_CODE = 20;

  /**
   * Creates a new exception.
   * 
   * @param message
   *        The message of this exception.
   */
  public PdfActParseException(String message) {
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
  public PdfActParseException(String message, Throwable cause) {
    super(EXIT_CODE, message, cause);
  }
}
