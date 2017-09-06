package pdfact.core.util.exception;

/**
 * The exception to throw on errors while parsing a PDF file.
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
   * Creates a new parse exception.
   * 
   * @param message
   *        The error message to show when the exception was caught.
   */
  public PdfActParseException(String message) {
    super(EXIT_CODE, message);
  }

  /**
   * Creates a new parse exception.
   * 
   * @param message
   *        The error message to show when the exception was caught.
   * @param cause
   *        The cause of this exception (can be used to trace the error).
   */
  public PdfActParseException(String message, Throwable cause) {
    super(EXIT_CODE, message, cause);
  }
}
