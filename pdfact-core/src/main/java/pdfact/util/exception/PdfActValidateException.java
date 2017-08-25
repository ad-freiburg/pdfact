package pdfact.util.exception;

/**
 * The exception to throw on errors while validating input parameters.
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
   * Creates a new validation exception.
   * 
   * @param message
   *        The error message to show when the exception was caught.
   */
  public PdfActValidateException(String message) {
    super(EXIT_CODE, message);
  }

  /**
   * Creates a new validation exception.
   * 
   * @param message
   *        The error message to show when the exception was caught.
   * @param cause
   *        The cause of this exception (can be used to trace the error).
   */
  public PdfActValidateException(String message, Throwable cause) {
    super(EXIT_CODE, message, cause);
  }
}
