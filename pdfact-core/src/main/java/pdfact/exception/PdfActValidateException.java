package pdfact.exception;

/**
 * The standard exception to throw on any validation error.
 * 
 * @author Claudius Korzen
 */
public class PdfActValidateException extends PdfActException {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -2555647293109903637L;

  /**
   * The default exit code of this exception.
   */
  protected static final int EXIT_CODE = 10;

  /**
   * Creates a new exception.
   * 
   * @param message
   *        The message of this exception.
   */
  public PdfActValidateException(String message) {
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
  public PdfActValidateException(String message, Throwable cause) {
    super(EXIT_CODE, message, cause);
  }
}
